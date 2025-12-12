package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Just prints to content of electionData to the standard output.>br/>
 * <b>This class needs heavy modification!</b>
 */
public class DutchConstituencyVotesTransformer implements VotesTransformer {
    private final Election election;

    /**
     * Creates a new transformer for handling the votes at the constituency level. It expects an instance of
     * Election that can be used for storing the results.
     * @param election the election in which the votes wil be stored.
     */
    public DutchConstituencyVotesTransformer(Election election) {
        this.election = election;
    }


    public Map<String, Integer> parse(InputStream inputStream) {

        Map<String, Integer> stemmenPerPartij = new HashMap<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList selections = doc.getElementsByTagNameNS("*", "Selection");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stemmenPerPartij;
    }

    @Override
    public void registerPartyVotes(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s party votes: %s\n", aggregated ? "Constituency" : "Municipality", electionData);
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s candidate votes: %s\n", aggregated ? "Constituency" : "Municipality", electionData);
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s meta data: %s\n", aggregated ? "Constituency" : "Municipality", electionData);
    }
}
