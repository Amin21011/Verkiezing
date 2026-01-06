package nl.hva.election_backend.service;

import nl.hva.election_backend.model.*;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.transformers.DutchNationalVotesTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Service
public class ProvinceService {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceService.class);
    private final Map<String, List<String>> provincieKieskringenMap;
    private final ResultRepository resultRepository;

    public ProvinceService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
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

    public List<ProvinceResult> getProvincieResultaten(int year) {
        List<ProvinceResult> resultaten = new ArrayList<>();
        Election election = loadElection(year);

        for (String provincie : provincieKieskringenMap.keySet()) {
            Map<String, Integer> totaalStemmen = new HashMap<>();

            for (String kieskring : provincieKieskringenMap.get(provincie)) {
                String resourcePath = String.format(
                        "TK2023_HvA_UvA/Telling_%d/Telling_TK%d_kieskring_%s.eml.xml",
                        year, year, kieskring
                );
                Resource resource = new ClassPathResource(resourcePath);

                if (!resource.exists()) {
                    logger.warn("Kon geen resource vinden voor kieskring {} (pad: {})", kieskring, resourcePath);
                    continue;
                }

                try (InputStream inputStream = resource.getInputStream()) {
                    DutchNationalVotesTransformer transformer = new DutchNationalVotesTransformer(election, resultRepository);
                    Map<String, Integer> stemmenKieskring = transformer.parse(inputStream);

                    stemmenKieskring.forEach((partij, stemmen) ->
                            totaalStemmen.merge(partij, stemmen, Integer::sum)
                    );

                } catch (Exception e) {
                    logger.error("Fout bij verwerken van kieskring {} (pad: {})", kieskring, resourcePath, e);
                }
            }
            resultaten.add(new ProvinceResult(provincie, totaalStemmen));
        }

        return resultaten;
    }

    public List<ProvinceResult> compareProvinces(int year, List<String> selectedProvinces) {
        List<ProvinceResult> allResults = getProvincieResultaten(year);
        List<ProvinceResult> filtered = new ArrayList<>();
        for (ProvinceResult result : allResults) {
            if (selectedProvinces.contains(result.getProvinceNaam())) {
                filtered.add(result);
            }
        }
        return filtered;
    }

    // Laadt partijen en kandidaten zonder ResultLoader
    private Election loadElection(int year) {
        Election election = new Election("TK" + year);

        // Partijen laden
        try {
            Resource partiesResource = new ClassPathResource("TK" + year + "/Verkiezingsdefinitie_TK" + year + ".eml.xml");
            if (partiesResource.exists()) {
                var docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                var doc = docBuilder.parse(partiesResource.getInputStream());
                NodeList partyNodes = doc.getElementsByTagName("PoliticalEntity");

                for (int i = 0; i < partyNodes.getLength(); i++) {
                    Node node = partyNodes.item(i);
                    if (node instanceof Element p) {
                        String id = p.getAttribute("Id");
                        String name = p.getElementsByTagName("RegisteredName").item(0).getTextContent();
                        election.addParty(new Party(id, name, 0)); // voteCount = 0
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Fout bij laden van partijen", e);
        }

        // Kandidaten laden
        try {
            Resource folderResource = new ClassPathResource("TK" + year);
            File folder = folderResource.getFile();
            File[] candidateFiles = folder.listFiles((d, n) -> n.toLowerCase().startsWith("kandidatenlijsten_") && n.endsWith(".eml.xml"));
            if (candidateFiles != null) {
                for (File f : candidateFiles) {
                    var docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    var doc = docBuilder.parse(f);
                    NodeList candidateNodes = doc.getElementsByTagName("Candidate");

                    for (int i = 0; i < candidateNodes.getLength(); i++) {
                        Node node = candidateNodes.item(i);
                        if (node instanceof Element c) {
                            String id = c.getAttribute("Id");
                            String name = c.getElementsByTagName("Name").item(0).getTextContent();
                            Element affiliation = (Element) c.getElementsByTagName("AffiliationIdentifier").item(0);
                            String partyId = affiliation.getAttribute("Id");
                            election.addCandidate(new Candidate(id, name, partyId));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Fout bij laden van kandidaten", e);
        }

        return election;
    }
}
