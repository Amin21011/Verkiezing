package nl.hva.election_backend.utils.xml.transformers;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.utils.xml.DefinitionTransformer;
import nl.hva.election_backend.utils.xml.TagAndAttributeNames;
import nl.hva.election_backend.model.Region;
import java.util.Map;

/**
 * Parses Dutch EML "Definition" XML files and registers regions (kieskringen)
 * and parties inside the given Election model.
 */

public class DutchDefinitionTransformer implements DefinitionTransformer {
    private final Election election;

    public DutchDefinitionTransformer(Election election) {
        this.election = election;
    }

    /**
     * Registers a region (Kieskring, gemeente, etc.) based on XML attributes.
     */
    @Override
    public void registerRegion(Map<String, String> electionData) {
        String regionId = electionData.get(TagAndAttributeNames.ID);
        String regionNumber = electionData.get(TagAndAttributeNames.REGION_NUMBER);
        String regionName = electionData.get(TagAndAttributeNames.REGION_NAME);

        if ((regionId == null && regionNumber == null) || regionName == null) {
            System.err.println("Region data incomplete: " + electionData);
            return;
        }

        // Loggen voor debug
        System.out.printf("Registered region -> ID: %s, Number: %s, Name: %s%n",
                regionId != null ? regionId : "unknown",
                regionNumber != null ? regionNumber : "unknown",
                regionName);
        Region region = new Region(regionId, regionNumber, regionName);
        election.addRegion(region);
    }


    @Override
    public void registerParty(Map<String, String> electionData) {
        String partyId = electionData.get(TagAndAttributeNames.ID);
        String name = electionData.get(TagAndAttributeNames.REGISTERED_APPELLATION);

        if (partyId != null && name != null && !name.isBlank()) {
            Party party = new Party(partyId, name, "", 0, "");
            election.addParty(party);
            System.out.printf("Registered party: %s (%s)%n", name, partyId);
        } else {
            System.err.println("Party registration failed. Data: " + electionData);
        }
    }
}