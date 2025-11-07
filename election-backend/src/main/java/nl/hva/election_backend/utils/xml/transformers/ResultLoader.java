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
 * Laadt partijen, kandidaten en resultaten uit XML-bestanden
 * en koppelt deze correct via de DutchResultTransformer.
 * Zorgt voor een schone herstart en voorkomt dubbele optellingen.
 */
public class ResultLoader {

    public static void loadResults(Election election, ResultRepository repository) throws Exception {
        System.out.println("🔄 [ResultLoader] Start import van verkiezingsdata...");

        repository.clearAll();
        election.getCandidates().forEach(c -> c.setVotes(0));
        election.getParties().forEach(p -> p.setVoteCount(0));

        DutchPartyParser partyParser = new DutchPartyParser();
        List<Party> parties = partyParser.parseParties("TK2023_HvA_UvA/Verkiezingsdefinitie_TK2023.eml.xml");
        election.getParties().addAll(parties);

        DutchCandidateParser candidateParser = new DutchCandidateParser();
        List<Candidate> candidates = candidateParser.parseCandidates(
                "TK2023_HvA_UvA/Kandidatenlijsten_TK2023_Amsterdam.eml.xml",
                parties
        );

        candidates.forEach(election::addCandidate);
        for (Candidate c : candidates) {
            election.findPartyById(c.getPartyId()).ifPresent(p -> p.addCandidate(c));
        }

        // 📦 4️⃣ Registreer partijen in repository
        repository.registerParties(parties);
        System.out.printf("%d partijen en %d kandidaten geladen.%n",
                election.getParties().size(), election.getCandidates().size());

        // 🧮 5️⃣ Setup transformer
        DutchResultTransformer transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);

        // 📂 6️⃣ XML-bestanden en regio's
        String[][] filesAndRegions = {
                {"/Resultaat_TK2023.eml.xml", "landelijk", "NL"},
                {"/Totaaltelling_TK2023.eml.xml", "landelijk", "NL"},
                {"/Telling_TK2023_gemeente_Amsterdam.eml.xml", "Amsterdam", "NL"}
        };

        XMLInputFactory factory = XMLInputFactory.newInstance();

        // 🧾 7️⃣ Verwerk elk XML-bestand afzonderlijk
        for (String[] fileRegion : filesAndRegions) {
            String file = fileRegion[0];
            InputStream is = ResultLoader.class.getResourceAsStream(file);
            if (is == null) {
                System.err.println("Bestand niet gevonden: " + file);
                continue;
            }

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
                            try {
                                int votes = Integer.parseInt(rawVotes.trim());
                                selectionData.put("ValidVotes", String.valueOf(votes));
                                if ("candidate".equals(currentType))
                                    selectionData.put("CandidateValidVotes", String.valueOf(votes));
                                else
                                    selectionData.put("PartyValidVotes", String.valueOf(votes));
                            } catch (NumberFormatException e) {
                                System.err.println("Ongeldige stemmenwaarde: " + rawVotes);
                            }
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
            is.close();
            System.out.println("Verwerkt: " + fileRegion[0]);
        }

        transformer.flushResults();

        System.out.println("[ResultLoader] Klaar — stemmen correct samengevoegd en opgeslagen.");
    }

    /**
     * Leest tekstinhoud van het huidige XML-element.
     */
    private static String readElementText(XMLStreamReader reader) throws Exception {
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) {
                text.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return text.toString().trim();
    }
}
