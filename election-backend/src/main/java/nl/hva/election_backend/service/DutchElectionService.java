package nl.hva.election_backend.service;
import nl.hva.election_backend.model.*;
import nl.hva.election_backend.utils.PathUtils;
import nl.hva.election_backend.utils.xml.*;
import nl.hva.election_backend.utils.xml.transformers.*;
import org.springframework.stereotype.Service;

/**
 * Service die verkiezingsdata uitleest uit EML-bestanden.
 */
@Service
public class DutchElectionService {

    private final ElectionParserFactory parserFactory;

    public DutchElectionService(ElectionParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public Election readResults(String electionId, String folderName) {
        System.out.println("Processing files...");
        Election election = new Election(electionId);

        var electionParser = parserFactory.createDutchParser(election);

        try {
            electionParser.parseResults(electionId, PathUtils.getResourcePath("/" + folderName));
            System.out.println("Dutch Election results: " + election);
            return election;
        } catch (Exception e) {
            System.err.println("Failed to process the election results!");
            e.printStackTrace();
            return null;
        }
    }
}
