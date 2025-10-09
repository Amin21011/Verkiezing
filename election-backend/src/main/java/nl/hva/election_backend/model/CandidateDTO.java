package nl.hva.election_backend.model;

public class CandidateDTO {

    private final String id;
    private final String shortCode;
    private final String firstName;
    private final String lastName;
    private final String partyId; // koppeling naar PartyDTO
    private int votes;

    public CandidateDTO(String id, String shortCode, String firstName, String lastName, String partyId) {
        this.id = id;
        this.shortCode = shortCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.partyId = partyId;
        this.votes = 0;
    }

    // Getters & setters
    public String getId() { return id; }
    public String getShortCode() { return shortCode; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPartyId() { return partyId; }

    public String getName() { return firstName + " " + lastName; }

    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }

    @Override
    public String toString() {
        return String.format("%s %s - ShortCode: %s - PartyId: %s - Aantal stemmen: %d",
                firstName, lastName, shortCode, partyId, votes);
    }
}
