package nl.hva.election_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "constituencies")
public class Constituencies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private  String name;

    public Constituencies() {}

    public Constituencies(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
