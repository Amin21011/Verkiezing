package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.utils.xml.CandidateTransformer;

import java.util.Map;

/**
 * Registreert kandidaten correct met zowel Id als ShortCode.
 */
public class DutchCandidateTransformer implements CandidateTransformer {

    private final Election election;

    public DutchCandidateTransformer(Election election) {
        this.election = election;
    }

    @Override
    public void registerCandidate(Map<String, String> electionData) {
        String candidateId = electionData.get("CandidateIdentifier-Id");
        String shortCode = electionData.get("CandidateIdentifier-ShortCode");
        String firstName = electionData.get("FirstName");
        String lastName = electionData.get("LastName");
        String partyId = electionData.get("AffiliationIdentifier-Id");

        if (candidateId == null || firstName == null || lastName == null) {
            System.out.println("Incomplete candidate data: " + electionData);
            return;
        }

        // Gebruik shortCode indien aanwezig, anders fallback op candidateId
        String finalShortCode = (shortCode != null && !shortCode.isBlank()) ? shortCode : candidateId;

        Candidate candidate = new Candidate(candidateId, finalShortCode, firstName, lastName, partyId);
        election.addCandidate(candidate);

        System.out.printf("Candidate toegevoegd: %s %s (Id=%s, ShortCode=%s, PartyId=%s)%n",
                firstName, lastName, candidateId, finalShortCode, partyId);
    }
}
