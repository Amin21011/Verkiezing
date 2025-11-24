package nl.hva.election_backend.model;

public class SearchRequest {
    private String name;

    public SearchRequest() {}

    public SearchRequest(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
