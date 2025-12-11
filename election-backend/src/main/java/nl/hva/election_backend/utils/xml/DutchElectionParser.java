package nl.hva.election_backend.utils.xml;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.repository.ElectionRepository;
import nl.hva.election_backend.utils.PathUtils;
import nl.hva.election_backend.utils.xml.transformers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class DutchElectionParser {
    private static final Logger log = LoggerFactory.getLogger(DutchElectionParser.class);
    private final Election election;
    private final ElectionRepository electionRepository;
    private final DefinitionTransformer definitionTransformer;
    private final CandidateTransformer candidateTransformer;
    private final DutchResultTransformer resultTransformer;
    private final DutchVotesTransformer votesTransformer;

    public DutchElectionParser(
            Election election,
            ElectionRepository electionRepository,
            DefinitionTransformer definitionTransformer,
            CandidateTransformer candidateTransformer,
            DutchResultTransformer resultTransformer,
            DutchVotesTransformer votesTransformer
    ) {
        this.election = election;
        this.electionRepository = electionRepository;
        this.definitionTransformer = definitionTransformer;
        this.candidateTransformer = candidateTransformer;
        this.resultTransformer = resultTransformer;
        this.votesTransformer = votesTransformer;
    }

    public void parseResults(String electionId, String folderName) throws IOException, SAXException {
        parseFiles(folderName, "Verkiezingsdefinitie_" + electionId,
                () -> new EMLHandler(definitionTransformer));
        parseFiles(folderName, "Kandidatenlijsten_" + electionId,
                () -> new EMLHandler(candidateTransformer));
        electionRepository.save(this.election);
        Supplier<DefaultHandler> voteHandlerSupplier = () -> new EMLHandler(votesTransformer);
        parseFiles(folderName, "Totaaltelling_" + electionId, voteHandlerSupplier);
        parseFiles(folderName, "Telling_" + electionId + "_kieskring", voteHandlerSupplier);
        parseFiles(folderName, "Telling_" + electionId + "_gemeente", voteHandlerSupplier);
        parseFiles(folderName, "Resultaat_" + electionId,
                () -> new EMLHandler(resultTransformer));
        log.info("Election {} parsed successfully.", electionId);
    }

    private void parseFiles(String folderName, String fileFilter, Supplier<DefaultHandler> handlerSupplier)
            throws IOException, SAXException {
        List<Path> files = PathUtils.findFilesToScan(folderName, fileFilter);
        files.sort(Comparator.comparing(Path::getFileName));

        if (files.isEmpty()) {
            log.warn("No files found for filter '{}' in folder '{}'", fileFilter, folderName);
            return;
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        for (Path p : files) {
            log.info("Processing file: {}", p.getFileName());
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(p.toFile()), 64 * 1024)) {
                SAXParser parser = factory.newSAXParser();
                DefaultHandler handler = handlerSupplier.get();

                if (handler instanceof EMLHandler) {
                    ((EMLHandler) handler).setFileName(p.getFileName().toString());
                }
                parser.parse(bis, handler);

            } catch (Exception e) {
                log.error("Error processing file {}: {}", p.getFileName(), e.getMessage());
                throw new SAXException("Failed to parse " + p.getFileName(), e);
            }
        }
    }
}