package nl.hva.election_backend.model;

import java.util.Map;

public class Question {
    private final String id;
    private final String text;
    private final Map<String, String> partyPositions; // partijnaam → standpunt

    public Question(String id, String text, Map<String, String> partyPositions) {
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
