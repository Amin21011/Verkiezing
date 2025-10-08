package nl.hva.election_backend.model;

/**
 * Represents a region in the Dutch election definition.
 */

public class RegionDTO {
    private final String number;
    private final String name;
    private final String category;

    public RegionDTO(String number, String name, String category) {
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
