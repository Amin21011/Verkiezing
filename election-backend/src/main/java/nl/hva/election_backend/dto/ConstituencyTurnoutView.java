package nl.hva.election_backend.dto;

import nl.hva.election_backend.model.Constituencies;

public interface ConstituencyTurnoutView {
    Constituencies getConstituencies();
    Long getVotes();
}