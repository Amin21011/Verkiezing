package nl.hva.election_backend.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "regions")
public class Region {
    @Id
    private String id;

    private String name;
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id")
    private Election election;

    public Region() {}

    public Region(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Election getElection() { return election; }
    public String getCategory() { return category; }

    public void setId(String id) { this.id = id; }
    public void setCategory(String category) { this.category = category; }
    public void setElection(Election e) { this.election = e; }
    public void setName(String name) { this.name = name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Region)) return false;
        Region region = (Region) o;
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}