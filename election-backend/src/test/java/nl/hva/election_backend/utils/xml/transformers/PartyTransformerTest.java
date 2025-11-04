package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Region;
import nl.hva.election_backend.utils.xml.DutchPartyParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test for DutchPartyParser that reads parties and regions from an EML XML file.
 * Prints parsed data (same as old main() method) and performs simple assertions.
 */
public class PartyTransformerTest {

    private DutchPartyParser parser;
    private List<Party> parties;
    private List<Region> regions;

    @BeforeEach
    void setUp() {
        parser = new DutchPartyParser();
        parties = parser.parseParties("Verkiezingsdefinitie_TK2023.eml.xml");
        regions = parser.parseRegions("Verkiezingsdefinitie_TK2023.eml.xml");
    }

    @Test
    void testParsedPartiesAndRegions() {
        // Output for readability (same as original main)
        System.out.println("Parsed Parties:");
        parties.forEach(p -> System.out.println(p.getName()));

        System.out.println("\nParsed Regions:");
        regions.forEach(r -> System.out.println(r));

        System.out.printf("%nTotal: %d parties, %d regions%n", parties.size(), regions.size());

        // Basic validation checks
        assertNotNull(parties, "Parties list should not be null");
        assertNotNull(regions, "Regions list should not be null");
        assertFalse(parties.isEmpty(), "There should be at least one party parsed");
        assertFalse(regions.isEmpty(), "There should be at least one region parsed");
    }
}
