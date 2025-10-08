package nl.hva.election_backend.repository;

import nl.hva.election_backend.model.PartyDTO;
import nl.hva.election_backend.model.ResultDTO;

import java.util.*;
import java.util.stream.Collectors;

public class ResultRepository {

    private final List<ResultDTO> results = new ArrayList<>();
    private final Map<String, String> partyNames = new HashMap<>();

    public void saveAll(List<ResultDTO> resultList) {
        if (resultList != null) {
            results.addAll(resultList);
        }
    }

    public List<ResultDTO> getAll() {
        return new ArrayList<>(results);
    }

    /** Find all results for a specific party */
    public List<ResultDTO> findByPartyId(String partyId) {
        if (partyId == null) return List.of();
        return results.stream()
                .filter(r -> partyId.equals(r.getPartyId()))
                .collect(Collectors.toList());
    }

    public void setPartyNames(List<PartyDTO> parties) {
        partyNames.clear();
        for (PartyDTO p : parties) {
            partyNames.put(p.getId(), p.getName());
        }
    }

    public String getPartyName(String partyId) {
        return partyNames.getOrDefault(partyId, "Onbekende partij");
    }

//    public void save(ResultDTO result) {
//        if (result != null) {
//            results.add(result);
//        }
//    }

//    public void clear() {
//        results.clear();
//    }
//
//    /** Find all results for a specific candidate */
//    public List<ResultDTO> findByCandidateId(String candidateId) {
//        if (candidateId == null) return List.of();
//        return results.stream()
//                .filter(r -> candidateId.equals(r.getCandidateId()))
//                .collect(Collectors.toList());
//    }
//
//    public Map<String, String> getPartyNames() {
//        return Collections.unmodifiableMap(partyNames);
//    }
//
//
//    /** Print all stored results */
//    public void printAll() {
//        for (ResultDTO result : results) {
//            String type = result.getCandidateId() == null ? "Party" : "Candidate";
//            String partyName = getPartyName(result.getPartyId());
//            System.out.printf("%s | PartyId: %s (%s) | CandidateId: %s | Votes: %d | Region: %s %s%n",
//                    type,
//                    result.getPartyId(),
//                    partyName,
//                    result.getCandidateId(),
//                    result.getVotes(),
//                    result.getRegionType(),
//                    result.getRegionId());
//        }
//    }

}
