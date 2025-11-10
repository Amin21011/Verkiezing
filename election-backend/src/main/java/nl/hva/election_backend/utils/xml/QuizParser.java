package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.model.Question;
import nl.hva.election_backend.model.Quiz;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.*;

public class QuizParser {

    // Leest een XML-bestand en zet het om in een Quiz-object
    public Quiz parseQuiz(String path) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) throw new RuntimeException("Kon XML niet vinden: " + path);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        List<Question> questions = new ArrayList<>();

        String id = null;
        String text = null;
        Map<String, String> positions = null;

        // Leest elk XML-element
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "question" -> {
                        id = reader.getAttributeValue(null, "id");
                        positions = new HashMap<>();
                    }
                    case "text" -> text = reader.getElementText();
                    case "party" -> {
                        String partyName = reader.getAttributeValue(null, "name");
                        String stance = reader.getElementText();
                        if (positions != null) {
                            positions.put(partyName, stance);
                        }
                    }
                }
            }
            // Einde van een vraag → maak Question-object
            if (event == XMLStreamConstants.END_ELEMENT && reader.getLocalName().equals("question")) {
                Map<String, String> safePositions = (positions != null) ? positions : new HashMap<>();
                questions.add(new Question(id, text, Map.copyOf(safePositions)));
            }
        }

        reader.close();
        return new Quiz(questions);
    }
}
