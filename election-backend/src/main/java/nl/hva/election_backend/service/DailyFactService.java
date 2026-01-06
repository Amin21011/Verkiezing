package nl.hva.election_backend.service;
import nl.hva.election_backend.dto.model.DailyFactResponse;
import nl.hva.election_backend.dto.SmallestPartyWinView;
import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Region;
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
                .orElseThrow(() ->
                        new IllegalStateException("Election TK2023 not found"));
    }

    public DailyFactService(ResultRepository resultRepository,
                            CandidateRepository candidateRepository, ElectionRepository electionRepository) {
        this.resultRepository = resultRepository;
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
    }

    public List<DailyFactResponse> getDailyFacts() {
        DailyFactResponse highestTurnout = getHighestTurnoutRegion();
        DailyFactResponse topCandidate = getTopCandidate();
        DailyFactResponse smallestPartyWin = getSmallestPartyWin();
        return List.of(highestTurnout, topCandidate, smallestPartyWin);
    }

    private DailyFactResponse getHighestTurnoutRegion() {
        var election = getElection();
        var result = resultRepository.findRegionTurnout(election);
        Region region = result.getFirst().getRegion();
        Long votes = result.getFirst().getVotes();

        return new DailyFactResponse(
                "region",
                "Hoogste opkomst",
                "De regio met de meeste uitgebrachte stemmen",
                region.getName() + " (" + votes + " stemmen)"
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
