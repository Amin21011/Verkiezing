package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.DutchElectionParser;
import nl.hva.election_backend.utils.xml.transformers.*;
import org.springframework.stereotype.Service;

@Service
public class ElectionParserFactory {

    public DutchElectionParser createDutchParser(Election election) {
        return new DutchElectionParser(
                new DutchDefinitionTransformer(election),
                new DutchCandidateTransformer(election),
                new DutchResultTransformer(election),
                new DutchNationalVotesTransformer(election),
                new DutchConstituencyVotesTransformer(election),
                new DutchMunicipalityVotesTransformer(election)
        );
    }
}
