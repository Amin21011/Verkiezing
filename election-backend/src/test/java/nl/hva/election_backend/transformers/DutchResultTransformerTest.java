package nl.hva.election_backend.transformers;
import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.*;
import nl.hva.election_backend.utils.xml.transformers.DutchResultTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DutchResultTransformerTest {
    private Party party;
    private Candidate candidate;
    private ResultRepository resultRepository;
    private CandidateRepository candidateRepository;
    private PartyRepository partyRepository;
    private DutchResultTransformer transformer;

    @BeforeEach
    void setUp() {
        Election election = new Election("TK2023");
        party = new Party("vvd", "VVD", 500);
        election.addParty(party);
        candidate = new Candidate("vvd_1", "Mark", "Rutte");

        party.addCandidate(candidate);
        election.addCandidate(candidate);

        resultRepository = mock(ResultRepository.class);
        candidateRepository = mock(CandidateRepository.class);
        RegionRepository regionRepository = mock(RegionRepository.class);
        partyRepository = mock(PartyRepository.class);

        transformer = new DutchResultTransformer(
                election,
                resultRepository,
                candidateRepository,
                regionRepository,
                partyRepository
        );
    }

    @Test
    void registerPartyVotes() {
        Map<String, String> data = Map.of(
                "AffiliationIdentifier-Id", "vvd",
                "ValidVotes", "1000"
        );
        transformer.registerPartyVotes(true, data);
        assertEquals(1500, party.getVoteCount());
        verify(resultRepository, times(1)).save(any(Result.class));
        verify(partyRepository).save(party);
    }

    @Test
    void registerCandidateVotes() {
        Map<String, String> data = Map.of(
                "AffiliationIdentifier-Id", "vvd",
                "CandidateIdentifier-Id", "1",
                "ValidVotes", "250",
                "Elected", "yes",
                "Ranking", "1"
        );

        transformer.registerPartyVotes(true, data);
        transformer.registerCandidateVotes(true, data);

        assertEquals(250, candidate.getVotes());
        assertTrue(candidate.isElected());
        assertEquals(1, candidate.getRanking());

        verify(candidateRepository).save(candidate);
        verify(resultRepository, times(2)).save(any(Result.class));
    }
}
