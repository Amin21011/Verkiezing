package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

     // Haal de top "x" partijen op, eventueel per verkiezingsjaar.
     // (filteren op jaar als Result klasse een 'year'-veld krijgt)

    public List<Party> getTopPartiesByYear(int year, int limit) {

        return resultRepository.findTopParties(limit);
    }

    /**
     * Haalt alle partijen op uit de repository.
     */

    public List<Party> getAllPartiesByYear(int year) {
        // In de toekomst kun je hier filtering per jaar of regio toevoegen.
        return resultRepository.findTopParties(Integer.MAX_VALUE);
    }
}
