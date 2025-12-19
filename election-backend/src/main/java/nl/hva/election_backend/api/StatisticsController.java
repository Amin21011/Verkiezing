package nl.hva.election_backend.api;
import nl.hva.election_backend.dto.DailyFactResponse;
import nl.hva.election_backend.service.DailyFactService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/statistics")
public class StatisticsController {
    private final DailyFactService dailyFactService;

    public StatisticsController(DailyFactService dailyFactService) {
        this.dailyFactService = dailyFactService;
    }

    @GetMapping("/daily-fact")
    public List<DailyFactResponse> getDailyFacts() {
        return dailyFactService.getDailyFacts();
    }
}
