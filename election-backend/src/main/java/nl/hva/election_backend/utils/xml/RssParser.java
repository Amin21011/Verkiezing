package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.NewsItem;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RssParser {


    private static final List<DateTimeFormatter> DATE_FORMATS = List.of(
            // "Thu, 09 Oct 2025 12:34:56 GMT" etc.
            DateTimeFormatter.RFC_1123_DATE_TIME,

            new java.time.format.DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("EEE, dd MMM yyyy HH:mm z")
                    .toFormatter(),

            DateTimeFormatter.ISO_OFFSET_DATE_TIME
    );

    public List<NewsItem> parse(String xml) {
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            // Security hardening
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setExpandEntityReferences(false);

            var db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");
            List<NewsItem> list = new ArrayList<>(items.getLength());

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = text(item, "title");
                String link = text(item, "link");
                String description = text(item, "description");
                String pubDate = text(item, "pubDate");

                OffsetDateTime publishedAt = parseDate(pubDate);
                String plainDescription = stripHtml(description).trim();

                list.add(new NewsItem(title, link, plainDescription, publishedAt));
            }

            return list;
        } catch (Exception e) {
            throw new RuntimeException("Fout bij parsen van RSS", e);
        }
    }

    private String text(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        Node n = nl.item(0);
        return n != null ? n.getTextContent() : "";
    }

    private String stripHtml(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", "");
    }

    private OffsetDateTime parseDate(String s) {
        if (s == null || s.isBlank()) return null;


        for (var fmt : DATE_FORMATS) {
            try {
                return OffsetDateTime.parse(s, fmt);
            } catch (Exception ignore) {}
        }


        try {
            Instant inst = Instant.parse(s);
            return OffsetDateTime.ofInstant(inst, ZoneOffset.UTC);
        } catch (Exception ignore) {}


        try {
            var fmt = new java.time.format.DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("yyyy-MM-dd HH:mm")
                    .toFormatter();
            return OffsetDateTime.of(
                    java.time.LocalDateTime.parse(s, fmt),
                    ZoneOffset.UTC
            );
        } catch (Exception ignore) {}

        return null;
    }
}
