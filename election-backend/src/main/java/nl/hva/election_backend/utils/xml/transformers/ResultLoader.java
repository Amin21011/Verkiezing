package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.ResultRepository;
import nl.hva.election_backend.utils.xml.DutchCandidateParser;
import nl.hva.election_backend.utils.xml.DutchPartyParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultLoader {

    public static void loadResults(Election election, ResultRepository repository) throws Exception {
        System.out.println("Start import van verkiezingsdata...");

        repository.clearAll();
        election.getCandidates().clear();
        election.getParties().clear();

        DutchPartyParser partyParser = new DutchPartyParser();
        List<Party> parties = partyParser.parseParties("TK2023_HvA_UvA/Verkiezingsdefinitie_TK2023.eml.xml");
        election.getParties().addAll(parties);

        DutchCandidateParser candidateParser = new DutchCandidateParser();
        URL folderUrl = ResultLoader.class.getResource("/TK2023_HvA_UvA");
        if (folderUrl == null) throw new IllegalStateException("Map TK2023_HvA_UvA niet gevonden in resources!");

        File folder = new File(folderUrl.toURI());
        File[] candidateFiles = folder.listFiles((d, n) -> n.toLowerCase().startsWith("kandidatenlijsten_") && n.endsWith(".eml.xml"));
        int totalCandidates = 0;

        if (candidateFiles != null) {
            for (File f : candidateFiles) {
                System.out.println("Kandidatenlijst laden: " + f.getName());
                List<Candidate> parsed = candidateParser.parseCandidates("TK2023_HvA_UvA/" + f.getName(), parties);
                parsed.forEach(election::addCandidate);
                totalCandidates += parsed.size();
            }
        }

        for (Candidate c : election.getCandidates()) {
            election.findPartyById(c.getPartyId()).ifPresent(p -> p.addCandidate(c));
        }
        repository.registerParties(parties);

        System.out.printf("%d partijen en %d kandidaten geladen.%n", election.getParties().size(), totalCandidates);

        File[] resultFiles = folder.listFiles((dir, name) ->
                (name.endsWith(".xml") || name.endsWith(".eml.xml"))
                        && !name.toLowerCase().contains("kandidatenlijst")
                        && !name.toLowerCase().contains("verkiezingsdefinitie"));

        if (resultFiles == null || resultFiles.length == 0) {
            System.err.println(" Geen telling-bestanden gevonden in TK2023_HvA_UvA/");
            return;
        }

        DutchResultTransformer transformer = new DutchResultTransformer(election);
        transformer.setRepository(repository);

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        for (File file : resultFiles) {
            String fileName = file.getName();
            if (fileName.toLowerCase().contains("totaaltelling")) continue;

            String regionType = "Onbekend";
            String regionName = fileName.replace(".eml.xml", "").replace(".xml", "")
                    .replace("Telling_TK2023_", "").trim();

            if (regionName.toLowerCase().contains("kieskring_")) {
                regionType = "Kieskring";
                regionName = regionName.substring(regionName.indexOf("kieskring_") + 10);
            } else if (regionName.toLowerCase().contains("gemeente_")) {
                regionType = "Gemeente";
                regionName = regionName.substring(regionName.indexOf("gemeente_") + 9);
            } else if (regionName.toLowerCase().contains("resultaat")) {
                regionType = "Landelijk";
                regionName = "Nederland";
            }

            regionName = regionName.replace("_", " ");
            if (!regionName.isEmpty()) {
                regionName = Character.toUpperCase(regionName.charAt(0)) + regionName.substring(1);
            }

            transformer.setRegionContext(regionType, regionName);
            System.out.printf("Verwerken van %s (%s %s)%n", fileName, regionType, regionName);

            try (InputStream is = ResultLoader.class.getResourceAsStream("/TK2023_HvA_UvA/" + fileName)) {
                if (is == null) continue;

                XMLStreamReader reader = factory.createXMLStreamReader(is);
                Map<String, String> selectionData = new HashMap<>();
                String currentAffiliationId = null;
                Candidate currentCandidate = null;

                while (reader.hasNext()) {
                    int event = reader.next();

                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String name = reader.getLocalName();

                        switch (name) {
                            case "AffiliationIdentifier" -> {
                                currentAffiliationId = reader.getAttributeValue(null, "Id");
                                if (currentAffiliationId != null) selectionData.put("AffiliationIdentifier-Id", currentAffiliationId.trim());
                            }
                            case "CandidateIdentifier" -> {
                                String candidateId = reader.getAttributeValue(null, "Id");
                                String shortCode = reader.getAttributeValue(null, "ShortCode");

                                if (candidateId != null) {
                                    final String cid = candidateId.trim();
                                    currentCandidate = election.getCandidates().stream()
                                            .filter(c -> c.getId().equals(cid))
                                            .findFirst()
                                            .orElse(null);
                                }

                                if (currentCandidate != null && shortCode != null)
                                    currentCandidate.setShortCode(shortCode.trim());
                            }
                            case "Gender" -> {
                                if (currentCandidate != null)
                                    currentCandidate.setGender(readElementText(reader));
                            }
                            case "QualifyingAddress" -> {
                                String residence = null;
                                while (reader.hasNext()) {
                                    int innerEvent = reader.next();
                                    if (innerEvent == XMLStreamConstants.START_ELEMENT &&
                                            "LocalityName".equals(reader.getLocalName())) {
                                        residence = readElementText(reader);
                                    } else if (innerEvent == XMLStreamConstants.END_ELEMENT &&
                                            "QualifyingAddress".equals(reader.getLocalName())) {
                                        break;
                                    }
                                }
                                if (currentCandidate != null && residence != null)
                                    currentCandidate.setResidence(residence);
                            }
                            case "ValidVotes" -> {
                                selectionData.put("ValidVotes", readElementText(reader));
                            }
                            case "TotalVotes" -> selectionData.put("TotalVotes", readElementText(reader));
                        }
                    }

                    if (event == XMLStreamConstants.END_ELEMENT) {
                        String name = reader.getLocalName();

                        if ("Selection".equals(name)) {
                            if (selectionData.containsKey("CandidateIdentifier-Id"))
                                transformer.registerCandidateVotes(true, selectionData);
                            else
                                transformer.registerPartyVotes(true, selectionData);
                            selectionData.clear();
                            currentCandidate = null;
                        } else if ("Affiliation".equals(name)) {
                            currentAffiliationId = null;
                        } else if ("Contest".equals(name)) {
                            transformer.registerMetadata(true, selectionData);
                            selectionData.clear();
                        }
                    }
                }
                reader.close();
                System.out.println("Verwerkt: " + fileName);
            }
        }

        election.getCandidates().forEach(c ->
                election.findPartyById(c.getPartyId())
                        .ifPresent(p -> c.setPartyName(p.getName()))
        );

        transformer.flushResults();

        System.out.println("Top 5 partijen na parsing:");
        election.getParties().stream()
                .sorted((a, b) -> Integer.compare(b.getVoteCount(), a.getVoteCount()))
                .limit(5)
                .forEach(p -> System.out.printf("→ %s: %d stemmen%n", p.getName(), p.getVoteCount()));

        System.out.println("Klaar — stemmen correct samengevoegd en opgeslagen.");
    }

    private static String readElementText(XMLStreamReader reader) throws Exception {
        StringBuilder text = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.CHARACTERS) text.append(reader.getText());
            else if (event == XMLStreamConstants.END_ELEMENT) break;
        }
        return text.toString().trim();
    }
}
