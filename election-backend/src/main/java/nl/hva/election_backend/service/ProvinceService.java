package nl.hva.election_backend.service;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.ProvinceResult;
import nl.hva.election_backend.utils.xml.transformers.DutchNationalVotesTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ProvinceService {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceService.class);

    private final Map<String, List<String>> provincieKieskringenMap;

    public ProvinceService() {
        this.provincieKieskringenMap = new HashMap<>();
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

        logger.info("ProvincieService initialized with {} provinces", provincieKieskringenMap.size());
    }

    public List<ProvinceResult> getProvincieResultaten() {
        List<ProvinceResult> resultaten = new ArrayList<>();
        for (String provincie : provincieKieskringenMap.keySet()) {
            Map<String, Integer> totaalStemmen = new HashMap<>();
            for (String kieskring : provincieKieskringenMap.get(provincie)) {
                String resourcePath = String.format(
                        "TK2023_HvA_UvA/Telling/Telling_TK2023_kieskring_%s.eml.xml",
                        kieskring
                );
                Resource resource = new ClassPathResource(resourcePath);

                if (!resource.exists()) {
                    logger.warn("Kon geen resource vinden voor kieskring {} (pad: {})", kieskring, resourcePath);
                    continue;
                }

                try (InputStream inputStream = resource.getInputStream()) {
                    Election election = new Election("TK2023");

                    DutchNationalVotesTransformer transformer = new DutchNationalVotesTransformer(election);

                    Map<String, Integer> stemmenKieskring = transformer.parse(inputStream);

                    stemmenKieskring.forEach((partij, stemmen) ->
                            totaalStemmen.merge(partij, stemmen, Integer::sum)
                    );

                } catch (IOException e) {
                    logger.error("Fout bij het verwerken van kieskring {} (resource: {})", kieskring, resourcePath, e);
                }
            }

            resultaten.add(new ProvinceResult(provincie, totaalStemmen));
        }
        return resultaten;
    }

}
