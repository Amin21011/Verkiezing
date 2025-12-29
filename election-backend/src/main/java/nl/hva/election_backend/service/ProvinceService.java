package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ConstituencyVotes;
import nl.hva.election_backend.model.ProvinceResult;
import nl.hva.election_backend.repository.ConstituencyVotesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProvinceService {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceService.class);

    private final ConstituencyVotesRepository constituencyVotesRepo;
    private final Map<String, List<String>> provincieKieskringenMap = new HashMap<>();

    public ProvinceService(ConstituencyVotesRepository constituencyVotesRepo) {
        this.constituencyVotesRepo = constituencyVotesRepo;

        provincieKieskringenMap.put("Groningen", List.of("Groningen"));
        provincieKieskringenMap.put("Friesland", List.of("Leeuwarden"));
        provincieKieskringenMap.put("Drenthe", List.of("Assen"));
        provincieKieskringenMap.put("Overijssel", List.of("Zwolle"));
        provincieKieskringenMap.put("Flevoland", List.of("Lelystad"));
        provincieKieskringenMap.put("Gelderland", List.of("Nijmegen", "Arnhem"));
        provincieKieskringenMap.put("Utrecht", List.of("Utrecht"));
        provincieKieskringenMap.put("Noord-Holland", List.of("Amsterdam", "Haarlem", "Den_Helder"));
        provincieKieskringenMap.put("Zuid-Holland", List.of("s-Gravenhage", "Rotterdam", "Dordrecht", "Leiden"));
        provincieKieskringenMap.put("Zeeland", List.of("Middelburg"));
        provincieKieskringenMap.put("Noord-Brabant", List.of("Tilburg", "s-Hertogenbosch"));
        provincieKieskringenMap.put("Limburg", List.of("Maastricht"));

        logger.info("ProvinceService initialized (DB-backed)");
    }

    public List<ProvinceResult> getProvincieResultaten(int year) {

        List<ConstituencyVotes> allVotes =
                constituencyVotesRepo.findByYear(year);

        List<ProvinceResult> resultaten = new ArrayList<>();

        for (String provincie : provincieKieskringenMap.keySet()) {

            Map<String, Integer> stemmenPerPartij = new HashMap<>();
            List<String> kieskringen = provincieKieskringenMap.get(provincie);

            for (ConstituencyVotes vote : allVotes) {

                if (!kieskringen.contains(vote.getConstituencies().getName())) {
                    continue;
                }

                stemmenPerPartij.merge(
                        vote.getPartyNames(),
                        vote.getVotes(),
                        Integer::sum
                );
            }

            resultaten.add(new ProvinceResult(provincie, stemmenPerPartij));
        }

        return resultaten;
    }

    public List<ProvinceResult> compareProvinces(int year, List<String> selectedProvinces) {
        return getProvincieResultaten(year).stream()
                .filter(p -> selectedProvinces.contains(p.getProvinceNaam()))
                .toList();
    }
}
