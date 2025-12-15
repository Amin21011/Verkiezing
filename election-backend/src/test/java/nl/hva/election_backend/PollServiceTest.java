package nl.hva.election_backend;

import nl.hva.election_backend.model.Poll;
import nl.hva.election_backend.repository.PollRepository;
import nl.hva.election_backend.service.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollServiceTest {

    private PollRepository pollRepository;
    private PollService pollService;

    @BeforeEach
    void setUp() {
        pollRepository = mock(PollRepository.class);
        pollService = new PollService(pollRepository);
    }

    @Test
    void createPoll_ShouldSavePoll() {
        Poll poll = new Poll("Vraag?", Arrays.asList("Ja", "Nee"));
        when(pollRepository.save(any(Poll.class))).thenReturn(poll);

        Poll result = pollService.createPoll("Vraag?", Arrays.asList("Ja", "Nee"));

        assertNotNull(result);
        assertEquals("Vraag?", result.getQuestion());
        verify(pollRepository, times(1)).save(any(Poll.class));
    }

    @Test
    void voteOnPoll_ShouldIncreaseVote_WhenValidIndex() {
        Poll poll = new Poll("Vraag?", Arrays.asList("Ja", "Nee"));
        poll.setVotes(Arrays.asList(0, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        pollService.voteOnPoll(1L, 0);

        ArgumentCaptor<Poll> captor = ArgumentCaptor.forClass(Poll.class);
        verify(pollRepository).save(captor.capture());

        List<Integer> votes = captor.getValue().getVotes();
        assertEquals(1, votes.get(0));
        assertEquals(0, votes.get(1));
    }

    @Test
    void voteOnPoll_ShouldDoNothing_WhenPollNotFound() {
        when(pollRepository.findById(1L)).thenReturn(Optional.empty());
        pollService.voteOnPoll(1L, 0);

        verify(pollRepository, never()).save(any());
    }

    @Test
    void voteOnPoll_ShouldDoNothing_WhenIndexInvalid() {
        Poll poll = new Poll("Vraag?", Arrays.asList("Ja", "Nee"));
        poll.setVotes(Arrays.asList(0, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        pollService.voteOnPoll(1L, 5); // ongeldig index

        verify(pollRepository, never()).save(any());
    }

    @Test
    void resetVote_ShouldDecreaseVote_NotBelowZero() {
        Poll poll = new Poll("Vraag?", Arrays.asList("Ja", "Nee"));
        poll.setVotes(Arrays.asList(2, 0));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));

        pollService.resetVote(1L, 0);
        pollService.resetVote(1L, 1); // al 0, mag niet negatief

        ArgumentCaptor<Poll> captor = ArgumentCaptor.forClass(Poll.class);
        verify(pollRepository, times(2)).save(captor.capture());

        List<Integer> votes = captor.getAllValues().get(1).getVotes();
        assertEquals(1, votes.get(0)); // eerste reset
        assertEquals(0, votes.get(1)); // mag niet negatief
    }

    @Test
    void deletePoll_ShouldCallRepository() {
        pollService.deletePoll(1L);
        verify(pollRepository, times(1)).deleteById(1L);
    }

    @Test
    void updatePoll_ShouldPreserveVotesForExistingOptions() {
        Poll poll = new Poll("Vraag?", Arrays.asList("Ja", "Nee"));
        poll.setVotes(Arrays.asList(2, 3));
        when(pollRepository.findById(1L)).thenReturn(Optional.of(poll));
        when(pollRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Poll updated = pollService.updatePoll(1L, "Nieuwe vraag?", Arrays.asList("Ja", "Nee", "Misschien"));

        assertEquals(Arrays.asList(2, 3, 0), updated.getVotes());
        assertEquals("Nieuwe vraag?", updated.getQuestion());
        assertEquals(Arrays.asList("Ja", "Nee", "Misschien"), updated.getOptions());
    }

    @Test
    void getAllPolls_ShouldReturnAll() {
        List<Poll> polls = Arrays.asList(new Poll("Q1", List.of("A", "B")), new Poll("Q2", List.of("C", "D")));
        when(pollRepository.findAll()).thenReturn(polls);

        List<Poll> result = pollService.getAllPolls();
        assertEquals(2, result.size());
    }

    @Test
    void activatePoll_ShouldDeactivateOthersAndActivateTarget() {
        Poll p1 = new Poll("Q1", List.of("A", "B"));
        Poll p2 = new Poll("Q2", List.of("C", "D"));
        p1.setActive(true);
        p2.setActive(false);

        when(pollRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        when(pollRepository.findById(2L)).thenReturn(Optional.of(p2));

        pollService.activatePoll(2L);

        assertFalse(p1.isActive());
        assertTrue(p2.isActive());
        verify(pollRepository).saveAll(Arrays.asList(p1, p2));
        verify(pollRepository).save(p2);
    }

    @Test
    void getActivePoll_ShouldReturnActivePollOrNull() {
        Poll p1 = new Poll("Q1", List.of("A", "B"));
        Poll p2 = new Poll("Q2", List.of("C", "D"));
        p1.setActive(false);
        p2.setActive(true);
        when(pollRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        Poll active = pollService.getActivePoll();
        assertEquals(p2, active);

        when(pollRepository.findAll()).thenReturn(Arrays.asList(p1));
        assertNull(pollService.getActivePoll());
    }
}
