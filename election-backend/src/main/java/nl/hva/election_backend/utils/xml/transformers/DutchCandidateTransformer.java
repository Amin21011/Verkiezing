package nl.hva.election_backend.utils.xml.transformers;

import nl.hva.election_backend.model.Candidate;
import nl.hva.election_backend.model.Election;
import nl.hva.election_backend.model.Party;
import nl.hva.election_backend.repository.CandidateRepository;
import nl.hva.election_backend.utils.xml.CandidateTransformer;
import nl.hva.election_backend.utils.xml.TagAndAttributeNames;

import java.util.Map;
import java.util.Optional;

public class DutchCandidateTransformer implements CandidateTransformer, TagAndAttributeNames {
    private final Election election;
    private final CandidateRepository candidateRepository;

    public DutchCandidateTransformer(Election election, CandidateRepository candidateRepository) {
        this.election = election;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void registerCandidate(Map<String, String> data) {
        String localId = data.get(CANDIDATE_IDENTIFIER_ID);
        String partyName = data.get(REGISTERED_NAME);

        if (partyName == null) partyName = data.get(REGISTERED_APPELLATION);
        if (localId == null || partyName == null) return;

        String partyDbId = rename(partyName);
        Optional<Party> partyOpt = election.findPartyById(partyDbId);

        if (partyOpt.isEmpty()) {
            System.err.println("⚠ Kan partij niet vinden (vergeet Definitions niet te draaien!): " + partyDbId);
            return;
        }

        Party party = partyOpt.get();
        String uniqueId = party.getId() + "_" + localId;

        if (election.getCandidateById(uniqueId).isEmpty()) {
            String first = data.getOrDefault(FIRST_NAME, "");
            String last = data.getOrDefault(LAST_NAME, "");
            String prefix = data.getOrDefault(NAME_PREFIX, "");
            String nameLine = data.getOrDefault(NAME_LINE, "");
            String gender = data.get(GENDER);

            if (first.isEmpty() && !nameLine.isEmpty()) first = nameLine;

            Candidate c = new Candidate(uniqueId, first, last);
            c.setNamePrefix(prefix);
            c.setElection(election);
            c.setParty(party);

            if (gender != null && !gender.isBlank()) {
                c.setGender(gender.trim());
            }
            election.addCandidate(c);
            candidateRepository.save(c);
        }
    }

    private String rename(String name) {
        return name.toLowerCase()
                .replace(" / ", "___")
                .replace(" - ", "___")
                .replace(" ", "_")
                .replace("/", "_")
                .replace("-", "_")
                .trim();
    }
}
