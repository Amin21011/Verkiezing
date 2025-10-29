package nl.hva.election_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String CandidateId;
    private  String shortCode;
    private  String firstName;
    private  String lastName;
    private  String partyId; // koppeling naar PartyDTO
    private int votes;

    public Candidate(String id, String shortCode, String firstName, String lastName, String partyId) {
        this.CandidateId = id;
        this.shortCode = shortCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.partyId = partyId;
        this.votes = 0;
    }

    public Candidate() {}

    // Getters & setters
    public String getCandidateId() { return CandidateId; }
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
