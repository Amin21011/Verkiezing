package nl.hva.election_backend.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int votes = 0;

    private Boolean elected;
    private Integer ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    private Election election;

    public Result() {}

    public Result(Election election, Region region, Party party, Candidate candidate, int votes) {
        this.election = election;
        this.region = region;
        this.party = party;
        this.candidate = candidate;
        this.votes = votes;
    }

    public Long getId() { return id; }
    public int getVotes() { return votes; }
    public Party getParty() { return party; }
    public Candidate getCandidate() { return candidate; }
    public Region getRegion() { return region; }
    public Election getElection() { return election; }
    public Boolean getElected() { return elected; }
    public Integer getRanking() { return ranking; }

    public void setVotes(int votes) { this.votes = votes; }
    public void setElected(Boolean elected) { this.elected = elected; }
    public void setRanking(Integer ranking) { this.ranking = ranking; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return Objects.equals(id, result.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}