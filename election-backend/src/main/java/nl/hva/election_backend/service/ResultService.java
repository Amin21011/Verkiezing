package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Result;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ResultService {
    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public List<Party> getTopParties(int limit) {
        try {
            Election election = new Election("TK2023");
            ResultLoader.loadResults(election, resultRepository);

            // bereken totale stemmen per partij
            for (Party party : election.getParties()) {
                int totalVotes = resultRepository.getAll().stream()
                        .filter(r -> party.getPartyId().equals(r.getPartyId()))
                        .mapToInt(Result::getVotes)
                        .sum();
                party.setVoteCount(totalVotes);
            }

            return election.getParties().stream()
                    .sorted(Comparator.comparingInt(Party::getVoteCount).reversed())
                    .limit(limit)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Party> getAllPartiesByYear(int year) {
        return resultRepository.findTopParties(Integer.MAX_VALUE);
    }
}
