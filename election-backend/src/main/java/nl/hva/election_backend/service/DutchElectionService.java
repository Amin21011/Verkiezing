package nl.hva.election_backend.service;

import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.ElectionRepository;
import nl.hva.election_backend.utils.PathUtils;
import nl.hva.election_backend.utils.xml.*;
import nl.hva.election_backend.utils.xml.transformers.*;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * A demo service for demonstrating how an EML-XML parser can be used inside a backend application.<br/>
 * <br/>
 * <i><b>NOTE: </b>There are some TODO's and FIXME's present that need fixing!</i>
 */
@Service
public class DutchElectionService {

    private final ElectionRepository electionRepository;

    public DutchElectionService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public Election readResults(String electionId, String folderName) {
        System.out.println("Processing files...");

        Election election = new Election(electionId);
        // TODO This lengthy construction of the parser should be replaced with a fitting design pattern!
        //  And refactoring the constructor while your at it is also a good idea.
        DutchElectionParser electionParser = new DutchElectionParser(
                new DutchDefinitionTransformer(election),
                new DutchCandidateTransformer(election),
                new DutchResultTransformer(election),
                new DutchNationalVotesTransformer(election),
                new DutchConstituencyVotesTransformer(election),
                new DutchMunicipalityVotesTransformer(election)
        );

        try {
            // Please note that you can also specify an absolute path to the folder!

            electionParser.parseResults(electionId, PathUtils.getResourcePath("/%s".formatted(folderName)));
            electionRepository.save(election);

            System.out.println("Dutch Election results: " + election);

            // Now is also the time to send the election information to a database for example.

            return election;
        } catch (IOException | XMLStreamException | NullPointerException | ParserConfigurationException | SAXException e) {
            // FIXME You should do here some proper error handling! The code below is NOT how you handle errors properly!
            System.err.println("Failed to process the election results!");
            e.printStackTrace();
            return null;
        }
    }

}
