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
import java.util.Optional;
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
        if (initialized) return;
        initialized = true;

        try {
            System.out.println("Initialiseren van verkiezingsresultaten...");

            election = new Election("TK2023");
            resultRepository.clearAll();

            ResultLoader.loadResults(election, resultRepository);
            resultRepository.registerParties(election.getParties());

            System.out.printf("ResultLoader klaar: %d partijen, %d kandidaten.%n",
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

        if (allCandidates.isEmpty()) {
            System.out.println("Geen kandidaten gevonden in ResultRepository.");
            return List.of();
        }

        Stream<Candidate> stream = allCandidates.stream();

        if (partyId != null && !partyId.isBlank()) {
            stream = stream.filter(c -> {
                if (c.getPartyId() == null) return false;
                String cid = c.getPartyId().trim();
                String pid = partyId.trim();
                return cid.equals(pid)
                        || cid.endsWith(pid)
                        || pid.endsWith(cid)
                        || cid.replaceAll("[^0-9]", "").equals(pid.replaceAll("[^0-9]", ""));
            });
        }

        List<Candidate> topCandidates = stream
                .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                .limit(limit)
                .peek(c -> {
                    Optional<Party> match = election.findPartyById(c.getPartyId());
                    match.ifPresent(p -> c.setPartyName(p.getName()));
                })
                .collect(Collectors.toList());

        int totalVotes = topCandidates.stream().mapToInt(Candidate::getVotes).sum();
        double avgVotes = topCandidates.isEmpty() ? 0 : (double) totalVotes / topCandidates.size();

        String partyName = (partyId != null && !partyId.isBlank())
                ? election.findPartyById(partyId).map(Party::getName).orElse("(Onbekende partij)")
                : "ALLE partijen";

        System.out.printf(
                "Top %d kandidaten geladen voor partij %s — totaal: %d stemmen, gemiddeld: %.0f%n",
                topCandidates.size(), partyName, totalVotes, avgVotes
        );

        if (topCandidates.isEmpty()) {
            System.out.printf("Geen kandidaten gevonden met partyId='%s' (controleer ID-consistentie in XML-bestanden)%n", partyId);
        }

        return topCandidates;
    }
}
