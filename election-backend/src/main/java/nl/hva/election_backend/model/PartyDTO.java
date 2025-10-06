package nl.hva.election_backend.model;

public class PartyDTO {
    private final String id;
    private final String name;
    private final String description;
    private final String leaderName;

    public PartyDTO(String id, String name, String description, String leaderName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leaderName = leaderName;
    }

    // Getters en setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLeaderName() { return leaderName; }

    @Override
    public String toString() {
        return name + " (leider: " + leaderName + ")";
    }
}
