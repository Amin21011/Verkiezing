package nl.hva.election_backend.utils.xml;

import nl.hva.election_backend.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class DutchElectionParser {
    private static final Logger log = LoggerFactory.getLogger(DutchElectionParser.class);

    private final DefinitionTransformer definitionTransformer;
    private final CandidateTransformer candidateTransformer;
    private final VotesTransformer resultTransformer;
    private final VotesTransformer nationalVotesTransformer;
    private final VotesTransformer constituencyVotesTransformer;
    private final VotesTransformer municipalityVotesTransformer;

    public DutchElectionParser(DefinitionTransformer definitionTransformer,
                               CandidateTransformer candidateTransformer,
                               VotesTransformer resultTransformer,
                               VotesTransformer nationalVotesTransformer,
                               VotesTransformer constituencyVotesTransformer,
                               VotesTransformer municipalityVotesTransformer) {
        this.definitionTransformer = definitionTransformer;
        this.candidateTransformer = candidateTransformer;
        this.resultTransformer = resultTransformer;
        this.nationalVotesTransformer = nationalVotesTransformer;
        this.constituencyVotesTransformer = constituencyVotesTransformer;
        this.municipalityVotesTransformer = municipalityVotesTransformer;
    }

    public void parseResults(String electionId, String folderName)
            throws IOException, XMLStreamException, ParserConfigurationException, SAXException {

        if (folderName == null || folderName.isBlank()) {
            throw new IllegalArgumentException("Folder name cannot be null or blank");
        }

        log.info("Loading election data from folder: {}", folderName);

        parseFiles(folderName, "Verkiezingsdefinitie_" + electionId, new EMLHandler(definitionTransformer));
        parseFiles(folderName, "Kandidatenlijsten_" + electionId, new EMLHandler(candidateTransformer));
        parseFiles(folderName, "Resultaat_" + electionId, new EMLHandler(resultTransformer));
        parseFiles(folderName, "Totaaltelling_" + electionId, new EMLHandler(nationalVotesTransformer));
        parseFiles(folderName, "Telling_" + electionId + "_kieskring", new EMLHandler(constituencyVotesTransformer));
        parseFiles(folderName, "Telling_" + electionId + "_gemeente", new EMLHandler(municipalityVotesTransformer));

        log.info("Election {} parsed successfully: [Definition, Candidates, Results✔]", electionId);
    }

    private void parseFiles(String folderName, String fileFilter, EMLHandler emlHandler)
            throws IOException, ParserConfigurationException, SAXException {

        List<Path> files = PathUtils.findFilesToScan(folderName, fileFilter);
        files.sort(Comparator.comparing(Path::getFileName));

        if (files.isEmpty()) {
            log.warn("No files found for filter '{}' in folder '{}'", fileFilter, folderName);
            return;
        }

        for (Path electionFile : files) {
            log.info("Processing file: {}", electionFile.getFileName());
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(electionFile.toString()), 64 * 1024)) {
                var factory = javax.xml.parsers.SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                var parser = factory.newSAXParser();
                emlHandler.setFileName(electionFile.toString());
                parser.parse(bis, emlHandler);
                log.debug("Processed file: {}", electionFile.getFileName());
            } catch (Exception e) {
                log.error("Error processing file {}: {}", electionFile.getFileName(), e.getMessage());
                throw e;
            }
        }
    }
}
