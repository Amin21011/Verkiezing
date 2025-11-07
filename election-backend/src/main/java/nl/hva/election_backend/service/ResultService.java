package nl.hva.election_backend.service;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResultService {
    private final ResultRepository resultRepository;
    private Election election;
    private boolean initialized = false;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @PostConstruct
    public void init() {
        if (initialized) return; // ✅ voorkomt dubbele loads
        initialized = true;

        try {
            System.out.println("🔄 Initialiseren van verkiezingsresultaten...");

            election = new Election("TK2023");
            resultRepository.clearAll(); // ✅ verwijder oude resultaten

            // ✅ Laad XML-resultaten en registreer partijen
            ResultLoader.loadResults(election, resultRepository);
            resultRepository.registerParties(election.getParties());

            System.out.printf("ResultLoader klaar: %d partijen, %d kandidaten%n",
                    election.getParties().size(), election.getCandidates().size());

        } catch (Exception e) {
            e.printStackTrace();
            initialized = false;
        }
    }

    public List<Party> getTopParties(int limit) {
        if (!initialized) init();

        return election.getParties().stream()
                .sorted(Comparator.comparingInt(Party::getVoteCount).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Candidate> getTopCandidatesByParty(String partyId, int limit) {
        if (!initialized) init();

        List<Candidate> allCandidates = resultRepository.getAllCandidates();

        Stream<Candidate> stream = allCandidates.stream();
        if (partyId != null && !partyId.isBlank()) {
            stream = stream.filter(c -> partyId.equals(c.getPartyId()));
        }

        List<Candidate> topCandidates = stream
                .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                .limit(limit)
                .peek(c -> election.findPartyById(c.getPartyId())
                        .ifPresent(p -> c.setPartyName(p.getName())))
                .collect(Collectors.toList());

        System.out.printf("Top %d kandidaten geladen voor partij %s (gem. stemmen: %d)%n",
                topCandidates.size(),
                partyId != null ? partyId : "ALLE partijen",
                topCandidates.stream().mapToInt(Candidate::getVotes).sum() / Math.max(1, topCandidates.size()));

        return topCandidates;
    }
}
