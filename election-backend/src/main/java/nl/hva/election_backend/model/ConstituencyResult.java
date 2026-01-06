package nl.hva.election_backend.model;

import java.util.Map;

public class ConstituencyResult {
    private String constituencyName;
    private Map<String, Integer> stemmenPerPartij;

    public ConstituencyResult(String constituencyName, Map<String, Integer> stemmenPerPartij) {
        this.constituencyName = constituencyName;
        this.stemmenPerPartij = stemmenPerPartij;
    }

    public String getConstituencyName() {
        return constituencyName;
    }

    public Map<String, Integer> getStemmenPerPartij() {
        return stemmenPerPartij;
    }
}
