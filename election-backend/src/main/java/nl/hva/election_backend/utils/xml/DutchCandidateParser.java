package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DutchCandidateParser {

    private static final String KR_NAMESPACE = "http://www.kiesraad.nl/extensions";

    /**
     * Parse candidates from the XML and link them to parties via AffiliationIdentifier.
     * Deze versie is namespace-onafhankelijk.
     */
    public List<Candidate> parseCandidates(String fileName, List<Party> parties) {
        List<Candidate> candidates = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is == null) {
                throw new IllegalArgumentException("XML file not found: " + fileName);
            }

            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            String candidateId = null;
            String shortCode = null;
            String firstName = null;
            String lastName = null;
            String partyId = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    switch (localName) {
                        case "CandidateIdentifier" -> {
                            candidateId = reader.getAttributeValue(null, "Id");
                            shortCode = reader.getAttributeValue(null, "ShortCode");
                        }
                        case "AffiliationIdentifier" -> {
                            partyId = reader.getAttributeValue(null, "Id");
                        }
                        case "FirstName" -> firstName = reader.getElementText().trim();
                        case "LastName" -> lastName = reader.getElementText().trim();
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT && "Candidate".equals(reader.getLocalName())) {
                    if (shortCode != null && firstName != null && lastName != null) {
                        // fallback: gebruik shortCode als ID als Id ontbreekt
                        String idToUse = candidateId != null ? candidateId : shortCode;

                        candidates.add(new Candidate(
                                idToUse,
                                shortCode,
                                firstName,
                                lastName,
                                partyId
                        ));

                        // System.out.printf("Parsed candidate: %s %s (id=%s, shortCode=%s, party=%s)%n",
                        //        firstName, lastName, idToUse, shortCode, partyId);
                    }

                    // reset voor volgende kandidaat
                    candidateId = shortCode = firstName = lastName = partyId = null;
                }
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return candidates;
    }
}
