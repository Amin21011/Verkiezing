package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.VotesTransformer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Just prints to content of electionData to the standard output.>br/>
 * <b>This class needs heavy modification!</b>
 */
public class DutchNationalVotesTransformer implements VotesTransformer {
    private final Election election;
    private final Map<String, Integer> stemmenPerPartij = new HashMap<>();

    public DutchNationalVotesTransformer(Election election) {
        this.election = election;
    }
    public Map<String, Integer> parse(InputStream inputStream) {
        return stemmenPerPartij;
    }


    @Override
    public void registerPartyVotes(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s party votes: %s\n", aggregated ? "National" : "Constituency", electionData);
    }

    @Override
    public void registerCandidateVotes(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s candidate votes: %s\n", aggregated ? "National" : "Constituency", electionData);
    }

    @Override
    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
        System.out.printf("%s meta data: %s\n", aggregated ? "National" : "Constituency", electionData);
    }
}
