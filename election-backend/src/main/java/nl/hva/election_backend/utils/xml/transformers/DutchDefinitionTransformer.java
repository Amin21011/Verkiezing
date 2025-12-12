package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.model.Region;
import nl.hva.election_backend.repository.PartyRepository;
import nl.hva.election_backend.repository.RegionRepository;
import nl.hva.election_backend.utils.xml.DefinitionTransformer;
import nl.hva.election_backend.utils.xml.TagAndAttributeNames;

import java.util.Map;

public class DutchDefinitionTransformer implements DefinitionTransformer, TagAndAttributeNames {
    private final Election election;
    private final PartyRepository partyRepository;
    private final RegionRepository regionRepository;

    public DutchDefinitionTransformer(Election election,
                                      PartyRepository partyRepository,
                                      RegionRepository regionRepository) {
        this.election = election;
        this.partyRepository = partyRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    public void registerRegion(Map<String, String> data) {
        String regionId =
                data.get(CONTEST_IDENTIFIER + "-" + ID) != null
                        ? data.get(CONTEST_IDENTIFIER + "-" + ID)
                        : data.get(REGION + "-" + REGION_NUMBER) != null
                        ? data.get(REGION + "-" + REGION_NUMBER)
                        : data.get(REGION + "-" + ID);

        String regionName =
                data.get(CONTEST_NAME) != null
                        ? data.get(CONTEST_NAME)
                        : data.get(REGION_NAME);

        String category =
                data.get(REGION + "-" + REGION_CATEGORY) != null
                        ? data.get(REGION + "-" + REGION_CATEGORY)
                        : "contest";

        if (regionId == null || regionName == null) {
            System.out.println("⚠ Region incomplete → " + data);
            return;
        }

        if (election.getRegionById(regionId).isEmpty()) {
            Region region = new Region(regionId, regionName, category);
            election.addRegion(region);
            System.out.println("✔ Region registered: " + regionName + " (" + regionId + "), type=" + category);
        }
    }

    @Override
    public void registerParty(Map<String, String> data) {
        String name = data.get(REGISTERED_NAME);
        if (name == null) name = data.get(REGISTERED_APPELLATION);
        if (name == null) return;
        String dbId = slugify(name);

        if (election.findPartyById(dbId).isEmpty()) {
            Party p = new Party(dbId, name, 0);
            election.addParty(p);
            partyRepository.save(p);
            System.out.println("✔ Party registered & saved: " + name + " (ID: " + dbId + ")");
        }
    }

    private String slugify(String name) {
        return name.toLowerCase()
                .replace(" / ", "___")
                .replace(" - ", "___")
                .replace(" ", "_")
                .replace("/", "_")
                .replace("-", "_")
                .trim();
    }
}