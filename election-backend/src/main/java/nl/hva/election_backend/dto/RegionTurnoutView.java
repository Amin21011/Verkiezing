package nl.hva.election_backend.dto;

import nl.hva.election_backend.model.Region;

public interface RegionTurnoutView {
    Region getRegion();
    Long getVotes();
}

