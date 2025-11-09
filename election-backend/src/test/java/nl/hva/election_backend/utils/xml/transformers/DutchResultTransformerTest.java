package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.DutchCandidateParser;
import nl.hva.election_backend.utils.xml.DutchPartyParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class DutchResultTransformerTest {

    private DutchResultTransformer transformer;
    private ResultRepository repository;
    private Election election;

    @BeforeEach
    void setUp() {
        repository = new ResultRepository();
        election = new Election("TK2023");

        DutchPartyParser partyParser = new DutchPartyParser();
        List<Party> parties = partyParser.parseParties("Verkiezingsdefinitie_TK2023.eml.xml");
        election.getParties().addAll(parties);

        DutchCandidateParser candidateParser = new DutchCandidateParser();
        List<Candidate> candidates = candidateParser.parseCandidates("Kandidatenlijsten_TK2023_Amsterdam.eml.xml", parties);
        candidates.forEach(election::addCandidate);

        for (Candidate candidate : candidates) {
            election.findPartyById(candidate.getPartyId()).ifPresent(p -> p.addCandidate(candidate));
        }

        repository.setPartyNames(election.getParties());
        transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);

        System.out.printf("Loaded %d parties, %d candidates%n", parties.size(), candidates.size());
    }

    @Test
    void testTransformerMultipleFiles() throws Exception {
        String[][] filesAndRegions = {
                {"/Resultaat_TK2023.eml.xml", "landelijk", "NL"},
                {"/Totaaltelling_TK2023.eml.xml", "landelijk", "NL"},
                {"/Telling_TK2023_kieskring_Amsterdam.eml.xml", "Amsterdam", "NL"},
                {"/Telling_TK2023_gemeente_Amsterdam.eml.xml", "Amsterdam", "NL"}
        };

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (String[] fileRegion : filesAndRegions) {
            InputStream is = getClass().getResourceAsStream(fileRegion[0]);
            assertNotNull(is, "XML file must exist: " + fileRegion[0]);

            transformer.setRegionContext(fileRegion[1], fileRegion[2]);
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            Map<String, String> selectionData = new HashMap<>();
            String lastPartyId = null;
            String currentType = null; // "party" of "candidate"

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    switch (localName) {
                        case "AffiliationIdentifier" -> {
                            String id = reader.getAttributeValue(null, "Id");
                            if (id != null) {
                                selectionData.put("AffiliationIdentifier-Id", id);
                                lastPartyId = id;
                            }
                            currentType = "party";
                        }
                        case "CandidateIdentifier" -> {
                            String id = reader.getAttributeValue(null, "Id");
                            String shortCode = reader.getAttributeValue(null, "ShortCode");
                            if (id != null) selectionData.put("CandidateIdentifier-Id", id);
                            if (shortCode != null) selectionData.put("CandidateIdentifier-ShortCode", shortCode);
                            if (!selectionData.containsKey("AffiliationIdentifier-Id") && lastPartyId != null) {
                                selectionData.put("AffiliationIdentifier-Id", lastPartyId);
                            }
                            currentType = "candidate";
                        }
                        case "ValidVotes" -> {
                            String vv = readElementText(reader);
                            selectionData.put("ValidVotes", vv);
                            if ("candidate".equals(currentType))
                                selectionData.put("CandidateValidVotes", vv);
                            else
                                selectionData.put("PartyValidVotes", vv);
                        }
                        case "TotalVotes" -> selectionData.put("TotalVotes", readElementText(reader));
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT) {
                    String end = reader.getLocalName();

                    if ("Selection".equals(end)) {
                        if ("candidate".equals(currentType))
                            transformer.registerCandidateVotes(true, selectionData);
                        else
                            transformer.registerPartyVotes(true, selectionData);

                        selectionData.clear();
                        currentType = null;
                    } else if ("Contest".equals(end)) {
                        transformer.registerMetadata(true, selectionData);
                        selectionData.clear();
                    }
                }
            }
            reader.close();
        }

        transformer.flushResults();

        System.out.println("\n=== Final Aggregated Results ===");
        election.getParties().forEach(p -> {
            System.out.printf("\n%s (%s) – Total Votes: %d%n", p.getName(), p.getId(), p.getVotes());
            p.getCandidates().stream()
                    .filter(c -> c.getVotes() > 0)
                    .sorted(Comparator.comparingInt(Candidate::getVotes).reversed())
                    .forEach(c -> System.out.printf("%s %s | Id=%s | ShortCode=%s | Votes=%d%n",
                            c.getFirstName(), c.getLastName(),
                            c.getId(), c.getShortCode(), c.getVotes()));
        });

        assertFalse(repository.getAll().isEmpty(), "ResultRepository should have entries");
    }

    private String readElementText(XMLStreamReader reader) throws Exception {
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) text.append(reader.getText());
            else if (event == XMLStreamConstants.END_ELEMENT) break;
        }
        return text.toString().trim();
    }
}
