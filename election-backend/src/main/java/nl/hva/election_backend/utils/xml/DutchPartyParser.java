package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Region;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DutchPartyParser {

    private static final String KR_NAMESPACE = "http://www.kiesraad.nl/extensions";

    public List<Party> parseParties(String fileName) {
        List<Party> parties = new ArrayList<>();
        try (InputStream is = DutchPartyParser.class.getResourceAsStream("/" + fileName)) {
            if (is == null) throw new IllegalArgumentException("XML file not found: " + fileName);

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT
                        && "RegisteredAppellation".equals(reader.getLocalName())
                        && KR_NAMESPACE.equals(reader.getNamespaceURI())) {

                    String name = reader.getElementText().trim();
                    if (!name.isEmpty()) {
                        parties.add(new Party(
                                String.valueOf(parties.size() + 1), // eenvoudige ID
                                name,
                                "", 0, ""));
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parties;
    }

    public List<Region> parseRegions(String fileName) {
        List<Region> regions = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream("/" + fileName)) {
            if (is == null) throw new IllegalArgumentException("XML file not found: " + fileName);

            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            String regionNumber = null;
            String regionName = null;
            String regionCategory = null;

            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if ("Region".equals(reader.getLocalName()) && KR_NAMESPACE.equals(reader.getNamespaceURI())) {
                        regionNumber = reader.getAttributeValue(null, "RegionNumber");
                        regionCategory = reader.getAttributeValue(null, "RegionCategory");
                    } else if ("RegionName".equals(reader.getLocalName()) && KR_NAMESPACE.equals(reader.getNamespaceURI())) {
                        regionName = reader.getElementText().trim();
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT
                        && "Region".equals(reader.getLocalName()) && KR_NAMESPACE.equals(reader.getNamespaceURI())) {
                    if (regionName != null) {
                        regions.add(new Region(regionNumber, regionName, regionCategory));
                    }
                    regionNumber = regionName = regionCategory = null;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regions;
    }
}
