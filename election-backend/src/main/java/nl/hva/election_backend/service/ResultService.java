package nl.hva.election_backend.service;

import jakarta.annotation.PostConstruct;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.transformers.ResultLoader;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            System.out.println("Initialiseren van verkiezingsresultaten..");
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
        resultRepository.aggregateCandidateVotes(election);

        return election.getCandidates().stream()
                .filter(c -> partyId == null || partyId.equals(c.getPartyId()))
                .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<Party> getPartyById(String id) {
        if (!initialized) init();
        resultRepository.aggregatePartyVotes(election);
        return election.findPartyById(id);
    }

    public Map<String, Integer> getVotesByParty() {
        if (!initialized) init();
        return resultRepository.getVotesByParty();
    }
}
