package nl.hva.election_backend.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "candidates")
public class Candidate {
    @Id
    private String id;
    private String namePrefix;
    private String firstName;
    private String lastName;
    private String fullName;
    private String gender;

    private int votes = 0;
    private boolean elected;
    private Integer ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id")
    private Election election;

    public Candidate() {}

    public Candidate(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        updateFullName();
    }

    public void updateFullName() {
        StringBuilder sb = new StringBuilder();

        if (firstName != null && !firstName.isBlank()) {
            sb.append(firstName.trim());
        }
        if (namePrefix != null && !namePrefix.isBlank()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(namePrefix.trim());
        }
        if (lastName != null && !lastName.isBlank()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(lastName.trim());
        }
        this.fullName = sb.toString();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getGender() { return gender; }
    public int getVotes() { return votes; }
    public String getId() { return id; }
    public Integer getRanking() { return ranking; }
    public Party getParty() { return party; }
    public Election getElection() { return election; }

    public boolean isElected() { return elected; }
    public void setId(String id) { this.id = id; }
    public void setGender(String gender) { this.gender = gender; }
    public void setVotes(int votes) { this.votes = votes; }
    public void setElected(boolean elected) { this.elected = elected; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }
    public void setElection(Election election) { this.election = election; }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        updateFullName();
    }

    public void setParty(Party party) {
        this.party = party;
        if (party != null && !party.getCandidates().contains(this)) {
            party.getCandidates().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Candidate candidate)) return false;
        return Objects.equals(id, candidate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getPartyId() {
        return party.getId();
    }

    public void setPartyName(String name) {
    }
}
