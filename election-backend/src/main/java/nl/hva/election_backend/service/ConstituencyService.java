package nl.hva.election_backend.service;

import nl.hva.election_backend.model.ConstituencyResult;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.transformers.DutchConstituencyVotesTransformer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConstituencyService {

    private static final List<String> Constituencies = List.of(
     "Amsterdam", "Arnhem", "Assen", "Bonaire", "Den_Helder", "Dordrecht", "Groningen", "Haarlem", "Leeuwarden", "Leiden", "Lelystad",
            "Maastricht", "Middelburg", "Nijmegen", "Rotterdam", "s-Gravenhage", "s-Hertogenbosch", "Tilburg", "Utrecht", "Zwolle"
    );

    public List<ConstituencyResult> getConstituencyResults(int year) {
        List<ConstituencyResult> result = new ArrayList<>();

        for (String constituency : Constituencies) {
            String path = String.format(
                    "TK2023_HvA_UvA/TK2023/Telling_TK2023_kieskring_%s.eml.xml",
                    constituency
            );
            Resource resource = new ClassPathResource(path);


            if (!resource.exists()) continue;

            try (InputStream input = resource.getInputStream()) {
                Election election = new Election("TK" + year);
                DutchConstituencyVotesTransformer transformer =
                        new DutchConstituencyVotesTransformer(election);

                Map<String, Integer> stemmen = transformer.parse(input);

                result.add(new ConstituencyResult(constituency, new HashMap<>(stemmen)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
