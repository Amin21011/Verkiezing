package nl.hva.election_backend.model;

import java.util.Map;

public class ProvinceResult {
    private String provincieNaam;
    private Map<String, Integer> stemmenPerPartij;

    public ProvinceResult(String provincieNaam, Map<String, Integer> stemmenPerPartij) {
        this.provincieNaam = provincieNaam;
        this.stemmenPerPartij = stemmenPerPartij;
    }

    public String getProvinceNaam() {
        return provincieNaam;
    }

    public Map<String, Integer> getStemmenPerPartij() {
        return stemmenPerPartij;
    }
}