package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.CandidateTransformer;

import java.util.Map;

/**
 * Just prints to content of electionData to the standard output.
 */

public class DutchCandidateTransformer implements CandidateTransformer {
    private final Election election;

    /**
     * Creates a new transformer for handling the candidate lists. It expects an instance of Election that can
     * be used for storing the candidates lists.
     * @param election the election in which the candidate lists wil be stored.
     */
    public DutchCandidateTransformer(Election election) {
        this.election = election;
    }

    @Override
    public void registerCandidate(Map<String, String> electionData) {
        String candidateId = electionData.get("CandidateIdentifier");
        String firstName = electionData.get("FirstName");
        String lastName = electionData.get("LastName");
        String partyId = electionData.get("AffiliationIdentifier");

        if (candidateId != null && firstName != null && lastName != null) {
            election.addCandidate(new Candidate(candidateId, candidateId, firstName, lastName, partyId));
        } else {
            System.out.println("Incomplete candidate data: " + electionData);
        }
    }
}
