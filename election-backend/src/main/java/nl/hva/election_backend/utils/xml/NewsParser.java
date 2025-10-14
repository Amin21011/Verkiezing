   package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.NewsItem;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Component
public class NewsParser {


    public List<NewsItem> parse(String fullText) {
        List<NewsItem> collectedNewsItems = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);

            XMLStreamReader xmlReader = factory.createXMLStreamReader(
                    new java.io.ByteArrayInputStream(fullText.getBytes(StandardCharsets.UTF_8)));

            boolean insideItem = false;
            String title = null, link = null, description = null, pubDate = null;

            while (xmlReader.hasNext()) {
                int event = xmlReader.next();

                if (event == XMLStreamReader.START_ELEMENT) {
                    String name =  xmlReader.getLocalName();

                    if ("item".equals(name)) {
                        insideItem = true;
                        title = link = description = pubDate = null;
                    } else if (insideItem) {
                        switch (name) {
                            case "title" -> title = xmlReader.getElementText().trim();
                            case "link" -> link = xmlReader.getElementText().trim();
                            case "description" -> description = xmlReader.getElementText().trim();
                            case "pubDate" -> pubDate = xmlReader.getElementText().trim();
                        }
                    }
                }

                if (event == XMLStreamReader.END_ELEMENT) {
                    if ("item".equals(xmlReader.getLocalName()) && insideItem) {
                        collectedNewsItems.add(new NewsItem(
                                title != null ? title : "",
                                link != null ? link : "",
                                description != null ? description : "",
                                pubDate != null ? pubDate : ""
                        ));
                        insideItem = false;
                    }
                }
            }

            xmlReader.close();
        } catch (Exception e) {
            throw new RuntimeException("Kon RSS niet parsen: " + e.getMessage(), e);
        }

        return collectedNewsItems;
    }
}
