package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.DutchCandidateParser;
import nl.hva.election_backend.utils.xml.DutchPartyParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Laadt partijen, kandidaten en resultaten uit XML en gebruikt DutchResultTransformer
 * om stemmen correct aan kandidaten en partijen te koppelen.
 */
public class ResultLoader {

    public static void loadResults(Election election, ResultRepository repository) throws Exception {
        DutchPartyParser partyParser = new DutchPartyParser();
        List<Party> parties = partyParser.parseParties("TK2023_HvA_UvA/Verkiezingsdefinitie_TK2023.eml.xml");
        election.getParties().addAll(parties);

        DutchCandidateParser candidateParser = new DutchCandidateParser();
        List<Candidate> candidates = candidateParser.parseCandidates("TK2023_HvA_UvA/Kandidatenlijsten_TK2023_Amsterdam.eml.xml", parties);
        candidates.forEach(election::addCandidate);
        for (Candidate c : candidates) {
            election.findPartyById(c.getPartyId()).ifPresent(p -> p.addCandidate(c));
        }

        repository.registerParties(parties);

        DutchResultTransformer transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);

        String[][] filesAndRegions = {
                {"/Resultaat_TK2023.eml.xml", "landelijk", "NL"},
                {"/Totaaltelling_TK2023.eml.xml", "landelijk", "NL"},
                {"/Telling_TK2023_gemeente_Amsterdam.eml.xml", "Amsterdam", "NL"}
        };

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (String[] fileRegion : filesAndRegions) {
            InputStream is = ResultLoader.class.getResourceAsStream(fileRegion[0]);
            if (is == null) continue;

            transformer.setRegionContext(fileRegion[1], fileRegion[2]);
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            Map<String, String> selectionData = new HashMap<>();
            String currentType = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String name = reader.getLocalName();

                    switch (name) {
                        case "AffiliationIdentifier" -> {
                            selectionData.put("AffiliationIdentifier-Id", reader.getAttributeValue(null, "Id"));
                            currentType = "party";
                        }
                        case "CandidateIdentifier" -> {
                            selectionData.put("CandidateIdentifier-Id", reader.getAttributeValue(null, "Id"));
                            selectionData.put("CandidateIdentifier-ShortCode", reader.getAttributeValue(null, "ShortCode"));
                            currentType = "candidate";
                        }
                        case "ValidVotes" -> {
                            String rawVotes = readElementText(reader);
                            int votes = 0;
                            try {
                                votes = Integer.parseInt(rawVotes);
                            } catch (NumberFormatException e) {
                                System.err.println("Ongeldige stemmenwaarde: " + rawVotes);
                            }
                            selectionData.put("ValidVotes", String.valueOf(votes)); // bewaar consistent als int-string
                            if ("candidate".equals(currentType))
                                selectionData.put("CandidateValidVotes", String.valueOf(votes));
                            else
                                selectionData.put("PartyValidVotes", String.valueOf(votes));
                        }
                        case "TotalVotes" -> selectionData.put("TotalVotes", readElementText(reader));
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT) {
                    String name = reader.getLocalName();

                    if ("Selection".equals(name)) {
                        if ("candidate".equals(currentType))
                            transformer.registerCandidateVotes(true, selectionData);
                        else
                            transformer.registerPartyVotes(true, selectionData);

                        selectionData.clear();
                        currentType = null;
                    } else if ("Contest".equals(name)) {
                        transformer.registerMetadata(true, selectionData);
                        selectionData.clear();
                    }
                }
            }
            reader.close();
        }

        // === 4️⃣ Stemmen samenvoegen ===
        transformer.flushResults();
        System.out.println("ResultLoader klaar — stemmen verwerkt en samengevoegd.");
    }

    private static String readElementText(XMLStreamReader reader) throws Exception {
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) text.append(reader.getText());
            else if (event == XMLStreamConstants.END_ELEMENT) break;
        }
        return text.toString().trim();
    }
}
