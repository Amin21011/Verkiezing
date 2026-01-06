package nl.hva.election_backend;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.CandidateRepository;
import nl.hva.election_backend.repository.PartyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Test
    void findByParty_Id_returnsCandidatesOfParty() {
        // Maak een partij aan en sla deze op in de database
        Party party = new Party("C1", "VVD", 0);
        partyRepository.save(party);

        // Maak een kandidaat en koppel deze aan de partij
        Candidate candidate = new Candidate("C1", "Mark", "Rutte");
        candidate.setParty(party);

        // Sla de kandidaat op in de database
        candidateRepository.save(candidate);

        // Haal alle kandidaten op die bij partij P1 horen
        List<Candidate> result =
                candidateRepository.findByParty_Id("C1");

        assertThat(result).hasSize(1);

        // Controleer of het de juiste kandidaat is
        assertThat(result.get(0).getFirstName()).isEqualTo("Mark");

        // Controleer of de kandidaat aan de juiste partij gekoppeld is
        assertThat(result.get(0).getParty().getId()).isEqualTo("C1");
    }

    @Test
    void findByFirstNameContainingIgnoreCase_works() {
        // Maak en sla een kandidaat op
        Candidate candidate = new Candidate("C2", "Geert", "Wilders");
        candidateRepository.save(candidate);

        // Zoek kandidaten waarvan de voornaam 'gee' bevat (case-insensitive)
        List<Candidate> result =
                candidateRepository.findByFirstNameContainingIgnoreCase("gee");

        // Verwacht één resultaat
        assertThat(result).hasSize(1);

        // Controleer of de juiste kandidaat is gevonden
        assertThat(result.get(0).getFirstName()).isEqualTo("Geert");
    }

    @Test
    void findByLastNameContainingIgnoreCase_works() {
        // Maak en sla een kandidaat op
        Candidate candidate = new Candidate("C3", "Dilan", "Yesilgöz");
        candidateRepository.save(candidate);

        // Zoek kandidaten waarvan de achternaam 'YES' bevat (hoofdletter-ongevoelig)
        List<Candidate> result =
                candidateRepository.findByLastNameContainingIgnoreCase("YES");

        // Verwacht één resultaat
        assertThat(result).hasSize(1);

        // Controleer of de juiste kandidaat is gevonden
        assertThat(result.get(0).getLastName()).isEqualTo("Yesilgöz");
    }
}
