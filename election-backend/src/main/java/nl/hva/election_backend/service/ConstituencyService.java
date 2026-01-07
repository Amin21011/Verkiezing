package nl.hva.election_backend.service;

import jakarta.transaction.Transactional;
import nl.hva.election_backend.model.Constituencies;
import nl.hva.election_backend.model.ConstituencyVotes;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.ConstituenciesRepository;
import nl.hva.election_backend.repository.ConstituencyVotesRepository;
import nl.hva.election_backend.utils.xml.transformers.DutchConstituencyVotesTransformer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ConstituencyService {

    private final ConstituenciesRepository constituenciesRepo;
    private final ConstituencyVotesRepository constituencyVotesRepo;

    public ConstituencyService(
            ConstituenciesRepository constituenciesRepo,
            ConstituencyVotesRepository constituencyVotesRepo
    ) {
        this.constituenciesRepo = constituenciesRepo;
        this.constituencyVotesRepo = constituencyVotesRepo;
    }

    public List<ConstituencyVotes> getVotesByYear(int year) {
        return constituencyVotesRepo.findByYear(year);
    }

    public void importConstituencyResults(int year) {
        importVotes(year);
    }


    private void importVotes(int year) {
        Election election = new Election("TK" + year);

        List<Constituencies> constituencies = constituenciesRepo.findAll();
        if (constituencies.isEmpty()) {
            return;
        }

        for (Constituencies constituency : constituencies) {

            String path = String.format(
                    "TK2023_HvA_UvA/TK%d/Telling_TK%d_kieskring_%s.eml.xml",
                    year, year, constituency.getName()
            );


            Resource resource = new ClassPathResource(path);
            if (!resource.exists()) {
                continue;
            }

            try (InputStream input = resource.getInputStream()) {

                DutchConstituencyVotesTransformer transformer =
                        new DutchConstituencyVotesTransformer(election);

                Map<String, Integer> stemmenPerPartij =
                        transformer.parse(input);

                for (Map.Entry<String, Integer> entry : stemmenPerPartij.entrySet()) {
                    ConstituencyVotes votes = new ConstituencyVotes(
                            entry.getKey(),
                            entry.getValue(),
                            year,
                            constituency
                    );
                    constituencyVotesRepo.save(votes);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
