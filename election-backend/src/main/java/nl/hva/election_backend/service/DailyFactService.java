package nl.hva.election_backend.service;
import nl.hva.election_backend.dto.model.DailyFactResponse;
import nl.hva.election_backend.dto.SmallestPartyWinView;
import nl.hva.election_backend.helpers.ForbiddenException;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DailyFactService {
    private final ResultRepository resultRepository;
    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    private Election getElection() {
        return electionRepository.findById("TK2023")
                .orElseThrow(() -> new ForbiddenException("Election TK2023 not found"));
    }

    public DailyFactService(ResultRepository resultRepository, CandidateRepository candidateRepository, ElectionRepository electionRepository) {
        this.resultRepository = resultRepository;
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
    }

    public List<DailyFactResponse> getDailyFacts() {
        DailyFactResponse highestTurnout = getHighestTurnoutMunicipality();
        DailyFactResponse topCandidate = getTopCandidate();
        DailyFactResponse smallestPartyWin = getSmallestPartyWin();
        return List.of(highestTurnout, topCandidate, smallestPartyWin);
    }

    private DailyFactResponse getHighestTurnoutMunicipality() {
        var result = resultRepository.findTopConstituencyByVotes();
        if (result.isEmpty()) {
            return new DailyFactResponse(
                    "municipality",
                    "Hoogste opkomst",
                    "Geen gegevens beschikbaar",
                    "-"
            );
        }

        var top = result.getFirst();
        String name = top.getConstituencies().getName();
        Long votes = top.getVotes();

        return new DailyFactResponse(
                "municipality",
                "Hoogste opkomst",
                "De gemeente met de meeste uitgebrachte stemmen",
                name + " (" + votes + " stemmen)"
        );
    }


    private DailyFactResponse getTopCandidate() {
        Candidate top = candidateRepository.findTopCandidates().getFirst();
        return new DailyFactResponse(
                "candidate",
                "Meeste stemmen",
                "De kandidaat met de meeste stemmen",
                top.getFullName() + " (" + top.getVotes() + ")"
        );
    }

    private DailyFactResponse getSmallestPartyWin() {
        var election = getElection();
        SmallestPartyWinView result = resultRepository
                .findSmallestPartyWinningRegion(election)
                .getFirst();

        return new DailyFactResponse(
                "party",
                "Kleinste partij gewonnen",
                "De regio waar de kleinste partij won",
                result.getPartyId() + " in " + result.getRegionName()
        );
    }
}
