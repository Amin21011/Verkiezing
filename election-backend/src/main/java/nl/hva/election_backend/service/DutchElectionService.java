package nl.hva.election_backend.service;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.ElectionRepository;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.PathUtils;
import nl.hva.election_backend.utils.xml.DutchElectionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DutchElectionService {
    private static final Logger log = LoggerFactory.getLogger(DutchElectionService.class);
    private final ElectionParserFactory parserFactory;
    private final ElectionRepository electionRepository;
    private final ResultRepository resultRepository;

    public DutchElectionService(ElectionParserFactory parserFactory,
                                ElectionRepository electionRepository,
                                ResultRepository resultRepository) {
        this.parserFactory = parserFactory;
        this.electionRepository = electionRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional
    public Election readResults(String electionId, String baseFolder) {
        log.info("Starting process for election: {}", electionId);
        Election election = new Election(electionId);
        DutchElectionParser parser = parserFactory.createDutchParser(election);
        Election saved = electionRepository.save(election);

        try {
            String folderPath = PathUtils.getResourcePath("/" + baseFolder);
            parser.parseResults(electionId, folderPath);
            aggregateElectionTotals(election);
            return saved;

        } catch (Exception e) {
            log.error("Unable to process election {}: {}", electionId, e.getMessage(), e);
            return null;
        }
    }

    private void aggregateElectionTotals(Election election) {
        election.getParties().forEach(p -> p.setVoteCount(0));
        election.getCandidates().forEach(c -> c.setVotes(0));

        List<Object[]> partyVotes = resultRepository.sumPartyVotes(election);

        for (Object[] row : partyVotes) {
            String partyKey = String.valueOf(row[0]);
            int totalVotes = ((Number) row[1]).intValue();

            election.findPartyById(partyKey)
                    .ifPresentOrElse(
                            p -> {
                                p.setVoteCount(totalVotes);
                                log.trace("Party '{}' ({}) -> {} votes",
                                        p.getName(), partyKey, totalVotes);
                            },
                            () -> log.warn("No Party found in Election {} for party key '{}' from sumPartyVotes",
                                    election.getId(), partyKey)
                    );
        }

        List<Object[]> candidateVotes = resultRepository.sumCandidateVotes(election);

        for (Object[] row : candidateVotes) {
            String candidateKey = String.valueOf(row[0]);
            int totalVotes = ((Number) row[1]).intValue();

            election.getCandidateById(candidateKey)
                    .ifPresentOrElse(
                            c -> {
                                c.setVotes(totalVotes);
                                log.trace("Candidate '{}' ({}) -> {} votes",
                                        c.getFullName(), candidateKey, totalVotes);
                            },
                            () -> log.warn("No Candidate found in Election {} for candidate key '{}' from sumCandidateVotes",
                                    election.getId(), candidateKey)
                    );
        }

        log.info("Aggregation complete. Top 5 parties:");
        election.getParties().stream()
                .sorted((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()))
                .limit(5)
                .forEach(p -> log.info("→ {} (id={}): {} votes",
                        p.getName(), p.getId(), p.getVoteCount()));
    }
}
