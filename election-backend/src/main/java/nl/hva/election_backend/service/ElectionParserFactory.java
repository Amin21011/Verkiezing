package nl.hva.election_backend.service;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.*;
import nl.hva.election_backend.utils.xml.DutchElectionParser;
import nl.hva.election_backend.utils.xml.transformers.*;
import org.springframework.stereotype.Service;

@Service
public class ElectionParserFactory {
    private final ResultRepository resultRepository;
    private final ElectionRepository electionRepository;
    private final RegionRepository regionRepository;
    private final CandidateRepository candidateRepository;
    private final PartyRepository partyRepository;

    public ElectionParserFactory(ResultRepository resultRepository,
                                 ElectionRepository electionRepository,
                                 RegionRepository regionRepository,
                                 CandidateRepository candidateRepository, PartyRepository partyRepository) {
        this.resultRepository = resultRepository;
        this.electionRepository = electionRepository;
        this.regionRepository = regionRepository;
        this.candidateRepository = candidateRepository;
        this.partyRepository = partyRepository;

    }

    public DutchElectionParser createDutchParser(Election election) {
        return new DutchElectionParser(election, electionRepository,
                new DutchDefinitionTransformer(election, partyRepository, regionRepository),
                new DutchCandidateTransformer(election, candidateRepository),
                new DutchResultTransformer(election, resultRepository, candidateRepository, regionRepository, partyRepository),
                new DutchVotesTransformer(election, partyRepository, candidateRepository)
        );
    }
}