package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String candidateId;
    private String shortCode;
    private String firstName;
    private String lastName;
    private String partyId;
    private String partyName;
    private int votes;

    public Candidate() {}

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
    public String getPartyName() { return partyName; }
    public int getVotes() { return votes; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPartyId(String partyId) { this.partyId = partyId; }
    public void setPartyName(String partyName) { this.partyName = partyName; }
    public void setVotes(int votes) { this.votes = votes; }

    public String getName() {
        if (firstName == null && lastName == null) return "(Unknown kandidaat)";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("%s %s - ShortCode: %s - PartyId: %s - Aantal stemmen: %d",
                firstName, lastName, shortCode, partyId, votes);
    }
}
