package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "constituency_votes")
public class ConstituencyVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    private String partyNames;
    private int votes;
    private int year;

    @ManyToOne
    private Constituencies constituencies;

    public ConstituencyVotes() {}

    public ConstituencyVotes(String partyNames, int votes, int year,  Constituencies constituencies) {
        this.partyNames = partyNames;
        this.votes = votes;
        this.year = year;
        this.constituencies = constituencies;
    }

    public long getId() {
        return id;
    }

    public String getPartyNames() {
        return partyNames;
    }

    public void setPartyNames(String partyNames) {
        this.partyNames = partyNames;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Constituencies getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(Constituencies constituencies) {
        this.constituencies = constituencies;
    }
}
