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
     * Parse candidates from the XML and link them to parties via AffiliationIdentifier.
     * Namespace-aware parsing die xnl-prefix automatisch verwerkt.
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
                        case "FirstName", "LastName" -> {
                            String value = reader.getElementText().trim();
                            if ("FirstName".equals(localName)) {
                                firstName = value;
                            } else {
                                lastName = value;
                            }
                        }
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT && "Candidate".equals(reader.getLocalName())) {
                    if ((candidateId != null || shortCode != null) && firstName != null && lastName != null) {
                        String idToUse = candidateId != null ? candidateId : shortCode;

                        candidates.add(new Candidate(
                                idToUse,
                                shortCode != null ? shortCode : idToUse,
                                firstName,
                                lastName,
                                partyId
                        ));
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
