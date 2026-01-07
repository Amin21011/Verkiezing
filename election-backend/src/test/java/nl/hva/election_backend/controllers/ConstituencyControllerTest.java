package nl.hva.election_backend.controllers;


import nl.hva.election_backend.api.ConstituencyController;
import nl.hva.election_backend.model.Constituencies;
import nl.hva.election_backend.model.ConstituencyVotes;
import nl.hva.election_backend.service.ConstituencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConstituencyController.class)
class ConstituencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConstituencyService constituencyService;

    @Test
    void importResults_callsService_andReturnsOk() throws Exception {
        int year = 2021;

        mockMvc.perform(post("/api/constituencies/import/{year}", year))
                .andExpect(status().isOk());

        verify(constituencyService).importConstituencyResults(year);
    }

    @Test
    void getResultsByYear_returnsVotesWithConstituency() throws Exception {
        int year = 2021;

        Constituencies constituency = new Constituencies();
        constituency.setName("Amsterdam");

        ConstituencyVotes votes = new ConstituencyVotes(
                "VVD",
                1234,
                year,
                constituency
        );

        when(constituencyService.getVotesByYear(year))
                .thenReturn(List.of(votes));

        mockMvc.perform(get("/api/constituencies/results/{year}", year))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partyNames").value("VVD"))
                .andExpect(jsonPath("$[0].votes").value(1234))
                .andExpect(jsonPath("$[0].year").value(year))
                .andExpect(jsonPath("$[0].constituencies.name").value("Amsterdam"));
    }
}
