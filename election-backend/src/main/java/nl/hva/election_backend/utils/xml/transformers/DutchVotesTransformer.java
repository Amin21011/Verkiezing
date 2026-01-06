package nl.hva.election_backend.utils.xml.transformers;
import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.*;
import nl.hva.election_backend.utils.xml.TagAndAttributeNames;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class DutchVotesTransformer implements VotesTransformer, TagAndAttributeNames {
    private static final Logger logger = LoggerFactory.getLogger(DutchVotesTransformer.class);
    private final Election election;
    private final PartyRepository partyRepository;
    private final CandidateRepository candidateRepository;
    private String currentPartyDbId = null;
    private final Map<String, String> xmlIdToDbIdCache = new HashMap<>();

    public DutchVotesTransformer(Election election,
                                 PartyRepository partyRepository,
                                 CandidateRepository candidateRepository) {
        this.election = election;
        this.partyRepository = partyRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void registerPartyVotes(boolean aggregated, Map<String, String> data) {
        String xmlId = data.get(AFFILIATION_IDENTIFIER + "-" + ATTR_ID);
        String votesStr = data.get(VALID_VOTES);
        if (xmlId == null || votesStr == null) return;

        String dbId = resolvePartyDbId(xmlId, data);
        if (dbId == null) {
            logger.warn("⚠ Party not found for XML ID: {}", xmlId);
            this.currentPartyDbId = null;
            return;
        }
        this.currentPartyDbId = dbId;

        election.findPartyById(dbId).ifPresent(party -> {
            int votes = parseVotes(votesStr);
            if (aggregated) {
                party.setVoteCount(party.getVoteCount() + votes);
                partyRepository.save(party);
            }
        });
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> data) {
        String localId = data.get(CANDIDATE_IDENTIFIER + "-" + ATTR_ID);
        String votesStr = data.get(VALID_VOTES);

        if (localId == null || this.currentPartyDbId == null || votesStr == null) {
            return;
        }

        int votes = parseVotes(votesStr);
        String uniqueDatabaseId = this.currentPartyDbId + "_" + localId; // bv "vvd_2"

        election.getCandidateById(uniqueDatabaseId).ifPresent(candidate -> {
            processCandidateMetadata(candidate, votes, aggregated, data);
        });
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> data) {
        this.currentPartyDbId = null;
    }

    private void processCandidateMetadata(Candidate candidate, int votes, boolean aggregated, Map<String, String> data) {
        boolean changed = false;
        if (aggregated && votes > 0) {
            candidate.setVotes(candidate.getVotes() + votes);
            changed = true;
        }

        if (data.containsKey(ELECTED)) {
            String electedStr = data.get(ELECTED);
            boolean isElected = "yes".equalsIgnoreCase(electedStr);
            if (candidate.isElected() != isElected) {
                candidate.setElected(isElected);
                changed = true;
            }
        }

        if (data.containsKey(RANKING)) {
            try {
                int rank = Integer.parseInt(data.get(RANKING));
                if (candidate.getRanking() == null || candidate.getRanking() != rank) {
                    candidate.setRanking(rank);
                    changed = true;
                }
            }
            catch (NumberFormatException ignored) {
            }
        }

        if (changed) {
            candidateRepository.save(candidate);
        }
    }

    private String resolvePartyDbId(String xmlId, Map<String, String> data) {
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
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
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