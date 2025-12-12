package nl.hva.election_backend.service;

import java.util.*;

public class CalculatorHelper {

    public static Map<String, Integer> calculate(
            Map<String, Integer> votes,
            int totalSeats,
            double thresholdPercent
    ) {
        Map<String, Integer> seats = new HashMap<>();

        int totalVotes = votes.values().stream().mapToInt(Integer::intValue).sum();
        int thresholdVotes = (int) (totalVotes * (thresholdPercent / 100));

        Map<String, Integer> eligible = new HashMap<>();
        for (var entry : votes.entrySet()) {
            if (entry.getValue() >= thresholdVotes) {
                eligible.put(entry.getKey(), entry.getValue());
            }
        }

        for (int i = 0; i < totalSeats; i++) {
            String bestParty = null;
            double bestScore = -1;

            for (var entry : eligible.entrySet()) {
                String party = entry.getKey();
                int vote = entry.getValue();
                int currentSeats = seats.getOrDefault(party, 0);
                double score = vote / (double) (currentSeats + 1);

                if (score > bestScore) {
                    bestScore = score;
                    bestParty = party;
                }
            }

            seats.put(bestParty, seats.getOrDefault(bestParty, 0) + 1);
        }
        return seats;
    }
}
