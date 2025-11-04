package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.xml.stream.*;
import java.io.InputStream;
import java.util.*;

/**
 * Parser voor kandidaten uit lokale kandidatenlijsten + landelijke verrijking (ShortCodes).
 */
public class DutchCandidateParser {

    private static final Logger log = LoggerFactory.getLogger(DutchCandidateParser.class);

    public List<Candidate> parseCandidates(String localListFile, List<Party> parties) {
        List<Candidate> candidates = parseCandidateList(localListFile, parties);
        enrichWithShortCodes(candidates, "Totaaltelling_TK2023.eml.xml");
        log.info("Parsed {} candidates (with  shortcodes)", candidates.size());
        return candidates;
    }

    // Reusable XML factory
    private XMLInputFactory createXmlFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        return factory;
    }

    private List<Candidate> parseCandidateList(String fileName, List<Party> parties) {
        List<Candidate> candidates = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is == null) throw new IllegalArgumentException("XML file not found: " + fileName);
            XMLStreamReader reader = createXmlFactory().createXMLStreamReader(is);

            String currentPartyId = null, candidateLocalId = null, firstName = null, lastName = null;
            boolean insideCandidate = false;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String tag = reader.getLocalName();
                    switch (tag) {
                        case "AffiliationIdentifier" -> currentPartyId = reader.getAttributeValue(null, "Id");
                        case "Candidate" -> insideCandidate = true;
                        case "CandidateIdentifier" -> candidateLocalId = reader.getAttributeValue(null, "Id");
                        case "FirstName" -> firstName = reader.getElementText().trim();
                        case "LastName" -> lastName = reader.getElementText().trim();
                    }
                }

                if (event == XMLStreamConstants.END_ELEMENT && "Candidate".equals(reader.getLocalName()) && insideCandidate) {
                    insideCandidate = false;
                    if (currentPartyId != null && candidateLocalId != null) {
                        String combinedId = currentPartyId + "-" + candidateLocalId;
                        candidates.add(new Candidate(combinedId, null, firstName, lastName, currentPartyId));
                    }
                    candidateLocalId = firstName = lastName = null;
                }
            }
            reader.close();
        } catch (Exception e) {
            log.error("Error parsing {}: {}", fileName, e.getMessage());
        }
        return candidates;
    }
    private void enrichWithShortCodes(List<Candidate> candidates, String fileName) {
        Map<String, Candidate> byLocalId = new HashMap<>();
        Map<String, Candidate> byFullName = new HashMap<>();

        for (Candidate c : candidates) {
            String localId = c.getId().substring(c.getId().indexOf('-') + 1);
            byLocalId.put(localId, c);
            String nameKey = (c.getFirstName() + " " + c.getLastName()).trim().toLowerCase();
            byFullName.put(nameKey, c);
        }

        int total = 0, unique = 0, matchedByName = 0, matchedById = 0;

        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is == null) {
                log.warn("Skipping enrichment (not found): {}", fileName);
                return;
            }

            XMLStreamReader reader = createXmlFactory().createXMLStreamReader(is);
            String lastShortCode = null;
            String lastFirst = null, lastLast = null;

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String tag = reader.getLocalName();

                    if ("CandidateIdentifier".equals(tag)) {
                        lastShortCode = reader.getAttributeValue(null, "ShortCode");
                        String id = reader.getAttributeValue(null, "Id");

                        if (lastShortCode != null && !lastShortCode.isBlank()) total++;

                        if (id != null && lastShortCode != null && !lastShortCode.isBlank()) {
                            Candidate c = byLocalId.get(id);
                            if (c != null && c.getShortCode() == null) {
                                c.setShortCode(lastShortCode);
                                matchedById++;
                                unique++;
                            }
                        }
                    }
                    else if ("FirstName".equals(tag)) {
                        lastFirst = reader.getElementText().trim();
                    }
                    else if ("LastName".equals(tag)) {
                        lastLast = reader.getElementText().trim();
                    }
                }

                // End of one Selection → try to match with name
                if (event == XMLStreamConstants.END_ELEMENT && "Selection".equals(reader.getLocalName())) {
                    if (lastShortCode != null && (lastFirst != null || lastLast != null)) {
                        String key = ((lastFirst != null ? lastFirst : "") + " " + (lastLast != null ? lastLast : "")).trim().toLowerCase();
                        Candidate c = byFullName.get(key);
                        if (c != null && c.getShortCode() == null) {
                            c.setShortCode(lastShortCode);
                            matchedByName++;
                            unique++;
                        }
                    }
                    // reset for next Selection
                    lastShortCode = null;
                    lastFirst = null;
                    lastLast = null;
                }
            }

            reader.close();
            log.info("Enriched {} unique candidates ({} total, {} by Id, {} by Name) from {}",
                    unique, total, matchedById, matchedByName, fileName);

        } catch (Exception e) {
            log.error("Error enriching {}: {}", fileName, e.getMessage());
        }
    }


}
