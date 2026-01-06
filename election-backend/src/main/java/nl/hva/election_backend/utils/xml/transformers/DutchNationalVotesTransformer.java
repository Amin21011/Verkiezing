package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.VotesTransformer;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DutchNationalVotesTransformer implements VotesTransformer {
    private final Election election;
    private final Map<String, Integer> stemmenPerPartij = new HashMap<>();
    private final ResultRepository resultRepository;

    public DutchNationalVotesTransformer(Election election, ResultRepository resultRepository) {
        this.election = election;
        this.resultRepository = resultRepository;
    }

    public Map<String, Integer> parse(InputStream inputStream) {
        try {
            var factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(inputStream);
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
    public void registerCandidateVotes(boolean aggregated, Map<String, String> data) {
        String candId = data.get("CandidateIdentifier-Id");
        String votesStr = data.get("ValidVotes");
        if (candId == null || votesStr == null) return;
        int votes = Integer.parseInt(votesStr);

        Candidate c = election.getCandidateById(candId).orElse(null);
        if (c == null) return;

        Region r = election.getRegionById("NL").orElseGet(() -> {
            Region nr = new Region("NL", "Nederland", "National");
            election.addRegion(nr);
            return nr;
        });

        resultRepository.save(new Result(election, r, c.getParty(), c, votes));
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {}

    public Map<String, Integer> getStemmenPerPartij() {
        return stemmenPerPartij;
    }
}
