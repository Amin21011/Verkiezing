package nl.hva.election_backend.repository;
import nl.hva.election_backend.dto.RegionTurnoutView;
import nl.hva.election_backend.dto.SmallestPartyWinView;
import nl.hva.election_backend.model.Result;
import nl.hva.election_backend.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByElection(Election election);

    @Query("""
        SELECT r.party.id, SUM(r.votes)
        FROM Result r
        WHERE r.election = :election AND r.party IS NOT NULL AND r.candidate IS NULL
        GROUP BY r.party.id
        """)
    List<Object[]> sumPartyVotes(@Param("election") Election election);

    @Query("""
        SELECT r.candidate.id, SUM(r.votes)
        FROM Result r
        WHERE r.election = :election AND r.candidate IS NOT NULL
        GROUP BY r.candidate.id
        """)
    List<Object[]> sumCandidateVotes(@Param("election") Election election);

    @Query("""
    SELECT 
        r.party.id AS partyId,
        r.region.name AS regionName,
        SUM(r.votes) AS votes
    FROM Result r
    WHERE r.party IS NOT NULL
      AND r.candidate IS NULL
      AND r.election = :election
    GROUP BY r.party.id, r.region.name
    ORDER BY SUM(r.votes) ASC
""")
    List<SmallestPartyWinView> findSmallestPartyWinningRegion(
            @Param("election") Election election
    );


    @Query("""
    SELECT r.region AS region, SUM(r.votes) AS votes
    FROM Result r
    WHERE r.region IS NOT NULL
      AND r.election = :election
    GROUP BY r.region
    ORDER BY SUM(r.votes) DESC
""")
    List<RegionTurnoutView> findRegionTurnout(@Param("election") Election election);
}