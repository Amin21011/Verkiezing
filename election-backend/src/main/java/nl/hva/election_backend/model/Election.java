package nl.hva.election_backend.model;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "elections")
public class Election {
    @Id
    private String id;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Party> parties = new ArrayList<>();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidate> candidates = new ArrayList<>();

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Region> regions = new ArrayList<>();

    public Election() {}

    public Election(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public List<Party> getParties() { return parties; }
    public List<Candidate> getCandidates() { return candidates; }
    public List<Region> getRegions() { return regions; }

    public void setId(String id) { this.id = id; }

    public void addParty(Party p) {
        if (p != null && !parties.contains(p)) {
            parties.add(p);
            p.setElection(this);
        }
    }

    public Optional<Party> findPartyById(String pid) {
        return parties.stream().filter(p -> p.getId().equals(pid)).findFirst();
    }

    public void addCandidate(Candidate c) {
        if (c != null && !candidates.contains(c)) {
            candidates.add(c);
            c.setElection(this);
        }
    }

    public Optional<Candidate> getCandidateById(String cid) {
        return candidates.stream().filter(c -> c.getId().equals(cid)).findFirst();
    }

    public void addRegion(Region r) {
        if (r != null && !regions.contains(r)) {
            regions.add(r);
            r.setElection(this);
        }
    }

    public Optional<Region> getRegionById(String rid) {
        return regions.stream().filter(r -> r.getId().equals(rid)).findFirst();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Election)) return false;
        Election election = (Election) o;
        return Objects.equals(id, election.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}