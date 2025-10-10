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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Kandidaten inladen
        DutchCandidateParser candidateParser = new DutchCandidateParser();
        List<Candidate> candidates = candidateParser.parseCandidates("Verkiezingsdefinitie_TK2023.eml.xml", parties);
        candidates.forEach(election::addCandidate);

        // Kandidaten koppelen aan hun partij
        for (Candidate candidate : candidates) {
            Party party = election.getParties().stream()
                    .filter(p -> p.getId().equals(candidate.getPartyId()))
                    .findFirst()
                    .orElse(null);
            if (party != null) party.addCandidate(candidate);
        }

        repository.setPartyNames(election.getParties());

        transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);
    }


    @Test
    void testTransformerMultipleFiles() throws Exception {
        String[][] filesAndRegions = {
                {"/Resultaat_TK2023.eml.xml", "landelijk", "NL"},
                {"/Totaaltelling_TK2023.eml.xml", "landelijk", "NL"},
                {"/Telling_TK2023_kieskring_Utrecht.eml.xml", "Utrecht", "NL"}
        };

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (String[] fileRegion : filesAndRegions) {
            InputStream is = getClass().getResourceAsStream(fileRegion[0]);
            assertNotNull(is, "XML file must exist: " + fileRegion[0]);

            transformer.setRegionContext(fileRegion[1], fileRegion[2]);
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            Map<String, String> selectionData = new HashMap<>();

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "AffiliationIdentifier" -> selectionData.put("AffiliationIdentifier-Id", reader.getAttributeValue(null, "Id"));
                        case "CandidateIdentifier" -> selectionData.put("CandidateIdentifier-ShortCode", reader.getAttributeValue(null, "ShortCode"));
                        case "ValidVotes" -> selectionData.put("ValidVotes", readElementText(reader));
                        case "TotalVotes" -> selectionData.put("TotalVotes", readElementText(reader));
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "Selection" -> {
                            transformer.registerPartyVotes(true, selectionData);
                            transformer.registerCandidateVotes(true, selectionData);
                            selectionData.clear();
                        }
                        case "Contest" -> {
                            transformer.registerMetadata(true, selectionData);
                            selectionData.clear();
                        }
                    }
                }
            }
            reader.close();
        }

        transformer.flushResults();

        System.out.println("\n=== Results ===");
        repository.getAll().stream()
                .sorted((r1, r2) -> (r1.getPartyId() != null ? r1.getPartyId() : "")
                        .compareTo(r2.getPartyId() != null ? r2.getPartyId() : ""))
                .forEach(result -> {
                    String type = result.getCandidateId() == null ? "Party" : "Candidate";
                    String partyName = repository.getPartyName(result.getPartyId());
                    System.out.printf("%s | PartyId: %s (%s) | CandidateId: %s | Votes: %d | Region: %s %s%n",
                            type,
                            result.getPartyId(),
                            partyName,
                            result.getCandidateId(),
                            result.getVotes(),
                            result.getRegionType(),
                            result.getRegionId());
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
