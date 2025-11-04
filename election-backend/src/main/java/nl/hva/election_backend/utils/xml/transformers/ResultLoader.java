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

public class ResultLoader {

    public static void loadResults(Election election, ResultRepository repository) throws Exception {
        DutchPartyParser partyParser = new DutchPartyParser();
        List<Party> parties = partyParser.parseParties("Verkiezingsdefinitie_TK2023.eml.xml");
        election.getParties().addAll(parties);

        // Candidates
        DutchCandidateParser candidateParser = new DutchCandidateParser();
        List<Candidate> candidates = candidateParser.parseCandidates("Kandidatenlijsten_TK2023_Amsterdam.eml.xml", parties);
        candidates.forEach(election::addCandidate);

        // Matching candidates
        for (Candidate candidate : candidates) {
            Party party = election.getParties().stream()
                    .filter(p -> p.getId().equals(candidate.getPartyId()))
                    .findFirst()
                    .orElse(null);
            if (party != null) party.addCandidate(candidate);
        }

        repository.registerParties(election.getParties());

        // Initialise ResultTransformer
        DutchResultTransformer transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);

        // Files and regions
        String[][] filesAndRegions = {
                {"/Resultaat_TK2023.eml.xml", "landelijk", "NL"},
                {"/Totaaltelling_TK2023.eml.xml", "landelijk", "NL"},
                {"/Telling_TK2023_kieskring_Utrecht.eml.xml", "Utrecht", "NL"}
        };

        XMLInputFactory factory = XMLInputFactory.newInstance();

        for (String[] fileRegion : filesAndRegions) {
            InputStream is = ResultLoader.class.getResourceAsStream(fileRegion[0]);
            if (is == null) continue;

            transformer.setRegionContext(fileRegion[1], fileRegion[2]);
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            Map<String, String> selectionData = new HashMap<>();
            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "AffiliationIdentifier" ->
                                selectionData.put("AffiliationIdentifier-Id", reader.getAttributeValue(null, "Id"));
                        case "CandidateIdentifier" ->
                                selectionData.put("CandidateIdentifier-ShortCode", reader.getAttributeValue(null, "ShortCode"));
                        case "ValidVotes" ->
                                selectionData.put("ValidVotes", readElementText(reader));
                        case "TotalVotes" ->
                                selectionData.put("TotalVotes", readElementText(reader));
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
