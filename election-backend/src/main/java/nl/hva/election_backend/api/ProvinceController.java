package nl.hva.election_backend.api;

import nl.hva.election_backend.model.ProvinceResult;
import nl.hva.election_backend.service.ProvinceService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {
    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping("/results/{year}")
    public List<ProvinceResult> getProvinceResults(@PathVariable int year) {
        return provinceService.getProvincieResultaten(year);
    }
}
