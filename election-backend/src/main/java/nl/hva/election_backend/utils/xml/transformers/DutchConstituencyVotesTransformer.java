//package nl.hva.election_backend.utils.xml.transformers;
//
//import nl.hva.election_backend.model.*;
//import nl.hva.election_backend.repository.ResultRepository;
//import nl.hva.election_backend.utils.xml.VotesTransformer;
//
//import java.util.Map;
//
///**
// * Just prints to content of electionData to the standard output.>br/>
// * <b>This class needs heavy modification!</b>
// */
//public class DutchConstituencyVotesTransformer implements VotesTransformer {
//    private final Election election;
//    private final ResultRepository repo;
//
//    /**
//     * Creates a new transformer for handling the votes at the constituency level. It expects an instance of
//     * Election that can be used for storing the results.
//     * @param election the election in which the votes wil be stored.
//     */
//    public DutchConstituencyVotesTransformer(Election election, ResultRepository repo) {
//        this.election = election;
//        this.repo = repo;
//    }
//
//    @Override
//    public void registerPartyVotes(boolean aggregated, Map<String, String> data) {
//
//        String partyId = data.get("AffiliationIdentifier-Id");
//        String votesStr = data.get("ValidVotes");
//        String regionId = data.get("ContestIdentifier-Id");
//
//        if (partyId == null || votesStr == null || regionId == null) return;
//
//        int votes = Integer.parseInt(votesStr);
//
//        Party p = election.findPartyById(partyId).orElse(null);
//        Region r = election.getRegionById(regionId).orElse(null);
//
//        if (p == null || r == null) return;
//
//        repo.save(new Result(election, r, p, null, votes));
//        System.out.printf("✔ CONST party votes %s (%s): %d\n", p.getName(), regionId, votes);
//    }
//
//
//    @Override
//    public void registerCandidateVotes(boolean aggregated, Map<String, String> data) {
//        String candId = data.get("CandidateIdentifier-Id");
//        String votesStr = data.get("ValidVotes");
//        String regionId = data.get("ContestIdentifier-Id");
//
//        if (candId == null || votesStr == null || regionId == null) return;
//
//        int votes = Integer.parseInt(votesStr);
//
//        Candidate c = election.getCandidateById(candId).orElse(null);
//        Region r = election.getRegionById(regionId).orElse(null);
//
//        if (c == null || r == null) return;
//
//        repo.save(new Result(election, r, c.getParty(), c, votes));
//
//        System.out.printf("✔ CONST candidate votes %s (%s): %d%n",
//                c.getFullName(), regionId, votes);
//    }
//
//
//    @Override
//    public void registerMetadata(boolean aggregated, Map<String, String> electionData) {
//        System.out.printf("%s meta data: %s\n", aggregated ? "Constituency" : "Municipality", electionData);
//    }
//}
