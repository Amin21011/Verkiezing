package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Just prints to content of electionData to the standard output.>br/>
 * <b>This class needs heavy modification!</b>
 */
public class DutchNationalVotesTransformer implements VotesTransformer {
    private final Election election;
    private final Map<String, Integer> stemmenPerPartij = new HashMap<>();

    /**
     * Creates a new transformer for handling the votes at the national level. It expects an instance of
     * Election that can be used for storing the results.
     * @param election the election in which the votes wil be stored.
     */
    public DutchNationalVotesTransformer(Election election) {
        this.election = election;
    }

    public Map<String, Integer> parse(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList selections = doc.getElementsByTagName("Selection");
            for (int i = 0; i < selections.getLength(); i++) {
                Element selection = (Element) selections.item(i);

                NodeList partijNodes = selection.getElementsByTagName("RegisteredName");
                NodeList stemmenNodes = selection.getElementsByTagName("ValidVotes");

                if (partijNodes.getLength() > 0 && stemmenNodes.getLength() > 0) {
                    String partijNaam = partijNodes.item(0).getTextContent().trim();
                    int stemmen = Integer.parseInt(stemmenNodes.item(0).getTextContent().trim());
                    stemmenPerPartij.merge(partijNaam, stemmen, Integer::sum);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stemmenPerPartij;
    }

    @Override
    public void registerPartyVotes(boolean aggregated, Map<String, String> electionData) {
        String partij = electionData.get("RegisteredName");
        String stemmenStr = electionData.get("ValidVotes");

        if (partij != null && stemmenStr != null) {
            try {
                int stemmen = Integer.parseInt(stemmenStr);
                stemmenPerPartij.merge(partij, stemmen, Integer::sum);
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s candidate votes: %s\n", aggregated ? "National" : "Constituency", electionData);
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s meta data: %s\n", aggregated ? "National" : "Constituency", electionData);
    }
    public Map<String, Integer> getStemmenPerPartij() {
        return stemmenPerPartij;
    }
}
