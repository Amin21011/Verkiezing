package nl.hva.election_backend.model;

public class Candidate {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String partyId;
    private String shortCode;
    private int votes;

    public Candidate(String id, String shortCode, String firstName, String lastName, String partyId) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Candidate ID cannot be null or blank");
        this.id = id;
        this.shortCode = shortCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.partyId = partyId;
        this.votes = 0;
    }

    public String getId() { return id; }
    public String getShortCode() { return shortCode; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPartyId() { return partyId; }

    public int getVotes() { return votes; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public void setVotes(int votes) { this.votes = votes; }

    public String getName() {
        if (firstName == null && lastName == null) return "(Unknown kandidaat)";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Candidate[id=%s, shortCode=%s, name=%s, partyId=%s, votes=%d]",
                id != null ? id : "-",
                shortCode != null ? shortCode : "-",
                getName(),
                partyId,
                votes
        );
    }
}
