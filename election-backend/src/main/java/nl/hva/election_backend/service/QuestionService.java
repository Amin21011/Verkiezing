package nl.hva.election_backend.service;

import java.util.Map;

public class QuestionService {
    private final String id;
    private final String text;
    private final Map<String, String> partyPositions; // partijnaam → standpunt

    public QuestionService(String id, String text, Map<String, String> partyPositions) {
        this.id = id;
        this.text = text;
        this.partyPositions = partyPositions;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getPartyPositions() {
        return partyPositions;
    }
}
