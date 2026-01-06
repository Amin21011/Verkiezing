package nl.hva.election_backend.dto;

public interface SmallestPartyWinView {
    String getPartyId();
    String getRegionName();
    Long getVotes();
}
