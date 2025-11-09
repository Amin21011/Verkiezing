package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DutchCandidateParser {

    /**
     * Parse candidates from XML, including all candidates under each Affiliation.
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

            String currentPartyId = null;
            String candidateId = null;
            String shortCode = null;
            String firstName = null;
            String lastName = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    switch (localName) {
                        case "AffiliationIdentifier" -> currentPartyId = reader.getAttributeValue(null, "Id");
                        case "CandidateIdentifier" -> {
                            candidateId = reader.getAttributeValue(null, "Id");
                            shortCode = reader.getAttributeValue(null, "ShortCode");
                        }
                        case "FirstName" -> firstName = reader.getElementText().trim();
                        case "LastName" -> lastName = reader.getElementText().trim();
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("Candidate".equals(localName)) {
                        if (candidateId != null && firstName != null && lastName != null && currentPartyId != null) {
                            // Gebruik echte ShortCode indien aanwezig
                            String finalShortCode = (shortCode != null && !shortCode.isBlank())
                                    ? shortCode
                                    : (firstName.substring(0, 1) + lastName).replaceAll("\\s+", "");

                            Candidate c = new Candidate(candidateId, finalShortCode, firstName, lastName, currentPartyId);
                            c.setPartyId(currentPartyId);
                            candidates.add(c);
                        }
                        // reset voor volgende kandidaat
                        candidateId = shortCode = firstName = lastName = null;
                    } else if ("Affiliation".equals(localName)) {
                        currentPartyId = null;
                    }
                }
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return candidates;
    }
}