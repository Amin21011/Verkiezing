package nl.hva.election_backend.utils.xml.transformers;
import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.*;
import nl.hva.election_backend.utils.xml.TagAndAttributeNames;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class DutchResultTransformer implements VotesTransformer, TagAndAttributeNames {
    private static final Logger logger = LoggerFactory.getLogger(DutchResultTransformer.class);
    private final Election election;
    private final ResultRepository resultRepository;
    private final CandidateRepository candidateRepository;
    private final RegionRepository regionRepository;
    private final PartyRepository partyRepository;
    private String currentPartyDbId = null;
    private final Map<String, String> xmlIdToDbIdCache = new HashMap<>();

    public DutchResultTransformer(Election election,
                                  ResultRepository resultRepository,
                                  CandidateRepository candidateRepository,
                                  RegionRepository regionRepository,
                                  PartyRepository partyRepository) {
        this.election = election;
        this.resultRepository = resultRepository;
        this.candidateRepository = candidateRepository;
        this.regionRepository = regionRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public void registerPartyVotes(boolean aggregated, Map<String, String> data) {
        String xmlId = data.get(AFFILIATION_IDENTIFIER + "-" + ATTR_ID);
        if (xmlId != null) {
            String dbId = resolvePartyDbId(xmlId, data);
            if (dbId != null) {
                this.currentPartyDbId = dbId;
            } else {
                this.currentPartyDbId = null;
                logger.warn("Kon partij niet resolven voor XML ID: {}", xmlId);
            }
        }
        String votesStr = data.get(VALID_VOTES);
        if (votesStr == null || this.currentPartyDbId == null) return;

        election.findPartyById(this.currentPartyDbId).ifPresent(party -> {
            int votes = parseVotes(votesStr);
            Region region = determineRegion(data);

            if (votes > 0) {
                resultRepository.save(new Result(election, region, party, null, votes));
            }
            if (aggregated && votes > 0) {
                party.setVoteCount(party.getVoteCount() + votes);
                partyRepository.save(party);
            }
        });
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> data) {
        String localId = data.get(CANDIDATE_IDENTIFIER + "-" + ATTR_ID);
        String effectivePartyId = this.currentPartyDbId;
        if (effectivePartyId == null && data.containsKey("partyId")) {
            effectivePartyId = resolvePartyDbId(data.get("partyId"), data);
        }
        if (effectivePartyId == null || localId == null) {
            return;
        }

        String uniqueDatabaseId = effectivePartyId + "_" + localId;
        int votes = parseVotes(data.get(VALID_VOTES));

        election.getCandidateById(uniqueDatabaseId).ifPresentOrElse(candidate -> {
            Region region = determineRegion(data);
            if (votes > 0) {
                Result result = new Result(election, region, candidate.getParty(), candidate, votes);
                resultRepository.save(result);
            }

            boolean changed = false;
            if (aggregated && votes > 0) {
                candidate.setVotes(candidate.getVotes() + votes);
                changed = true;
            }

            changed |= updateElectedStatus(candidate, data);
            changed |= updateRanking(candidate, data);

            if (changed) {
                candidateRepository.save(candidate);
            }
        }, () -> {
            logger.warn("Kandidaat niet gevonden in DB: {}", uniqueDatabaseId);
        });
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> data) {
        this.currentPartyDbId = null;
    }

    private boolean updateElectedStatus(Candidate candidate, Map<String, String> data) {
        String electedVal = data.get(ELECTED);
        if (electedVal == null) electedVal = data.get("Elected");
        if (electedVal == null) return false;

        boolean isElected = "yes".equalsIgnoreCase(electedVal.trim())
                || "true".equalsIgnoreCase(electedVal.trim());

        if (candidate.isElected() != isElected) {
            candidate.setElected(isElected);
            return true;
        }
        return false;
    }

    private boolean updateRanking(Candidate candidate, Map<String, String> data) {
        String rankingVal = data.get(RANKING);
        if (rankingVal == null) rankingVal = data.get("Ranking");

        if (rankingVal != null && !rankingVal.isBlank()) {
            try {
                int rank = Integer.parseInt(rankingVal.trim());
                if (candidate.getRanking() == null || !candidate.getRanking().equals(rank)) {
                    candidate.setRanking(rank);
                    return true;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }

    private Region determineRegion(Map<String, String> data) {
        String regionId = data.get(CONTEST_IDENTIFIER + "-" + ATTR_ID);
        if (regionId == null) regionId = data.get(REPORTING_UNIT_IDENTIFIER + "-" + ATTR_ID);
        if (regionId == null) regionId = "NL";
        String name = data.getOrDefault(CONTEST_NAME, "Region " + regionId);
        String finalId = regionId;

        return election.getRegionById(finalId).orElseGet(() -> {
            Region r = new Region(finalId, name, "Auto");
            election.addRegion(r);
            return regionRepository.save(r);
        });
    }

    private String resolvePartyDbId(String xmlId, Map<String, String> data) {
        if (xmlId == null) return null;
        if (xmlIdToDbIdCache.containsKey(xmlId)) return xmlIdToDbIdCache.get(xmlId);

        String name = data.get(REGISTERED_NAME);
        if (name == null) name = data.get(REGISTERED_APPELLATION);

        if (name != null) {
            String slug = rename(name);
            if (election.findPartyById(slug).isPresent()) {
                xmlIdToDbIdCache.put(xmlId, slug);
                return slug;
            }
        }
        if (election.findPartyById(xmlId).isPresent()) return xmlId;

        return null;
    }

    private int parseVotes(String s) {
        try { return s == null ? 0 : Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private String rename(String name) {
        return name.toLowerCase()
                .replace(" / ", "___")
                .replace(" - ", "___")
                .replace(" ", "_")
                .replace("/", "_")
                .replace("-", "_")
                .trim();
    }
}