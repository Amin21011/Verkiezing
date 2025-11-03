package nl.hva.election_backend.model;

import jakarta.persistence.*;

/**
 * Represents a region in the Dutch election definition.
 */
@Entity
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  String number;
    private  String name;
    private  String category;

    public Region() {}

    public Region(String number, String name, String category) {
        this.number = number;
        this.name = name;
        this.category = category;
    }

    public String getNumber() { return number; }
    public String getName() { return name; }
    public String getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("Region #%s: %s (%s)", number, name, category);
    }
}
