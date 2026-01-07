package nl.hva.election_backend.services;


import nl.hva.election_backend.model.Constituencies;
import nl.hva.election_backend.model.ConstituencyVotes;
import nl.hva.election_backend.repository.ConstituenciesRepository;
import nl.hva.election_backend.repository.ConstituencyVotesRepository;
import nl.hva.election_backend.service.ConstituencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstituencyServiceTest {

    @Mock
    private ConstituenciesRepository constituenciesRepo;

    @Mock
    private ConstituencyVotesRepository constituencyVotesRepo;

    @InjectMocks
    private ConstituencyService constituencyService;


    @Test
    void getVotesByYear_returnsVotesFromRepository() {
        // Arrange
        ConstituencyVotes vote = new ConstituencyVotes(
                "VVD",
                1200,
                2023,
                null
        );

        when(constituencyVotesRepo.findByYear(2023))
                .thenReturn(List.of(vote));

        // Act
        List<ConstituencyVotes> result =
                constituencyService.getVotesByYear(2023);

        // Assert
        assertEquals(1, result.size());
        assertEquals("VVD", result.get(0).getPartyNames());
        assertEquals(1200, result.get(0).getVotes());

        verify(constituencyVotesRepo).findByYear(2023);
    }

    @Test
    void importConstituencyResults_doesNothing_whenNoConstituencies() {
        // Arrange
        when(constituenciesRepo.findAll())
                .thenReturn(List.of());

        // Act
        constituencyService.importConstituencyResults(2023);

        // Assert
        verify(constituenciesRepo).findAll();
        verifyNoInteractions(constituencyVotesRepo);
    }

    @Test
    void importConstituencyResults_savesVotes_whenXmlExists() {
        // Arrange
        Constituencies constituency = new Constituencies();
        constituency.setName("Amsterdam");

        when(constituenciesRepo.findAll())
                .thenReturn(List.of(constituency));

        // Act
        constituencyService.importConstituencyResults(2023);

        // Assert
        verify(constituenciesRepo).findAll();
        verify(constituencyVotesRepo, atLeastOnce()).save(any());
    }
}

