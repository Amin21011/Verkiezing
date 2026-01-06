package nl.hva.election_backend.api;

import nl.hva.election_backend.dto.FakeNewsRequest;
import nl.hva.election_backend.dto.model.FakeNewsResponse;
import nl.hva.election_backend.service.FakeNewsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class FakeNewsController {
    private final FakeNewsService service;

    public FakeNewsController(FakeNewsService service) {
        this.service = service;
    }

    @PostMapping("/fake-news")
    public FakeNewsResponse analyze(@RequestBody FakeNewsRequest request) {
        return service.analyze(request);
    }
}
