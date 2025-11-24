package nl.hva.election_backend.model;

import java.util.List;

public class ProvinceCompareRequest {
    private int year;
    private List<String> provinces;

    public int getYear() {
        return year;
    }

    public List<String> getProvinces() {
        return provinces;
    }
}
