package mk.ukim.finki.wp.tests.mock_mvc;

import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.service.MatchLocationService;
import mk.ukim.finki.wp.tests.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;


    @MockBean
    private MatchLocationService matchLocationService;


    @BeforeEach
    public void setup() {
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShowMatches_NoPriceNoType() throws Exception {
        // Mocking service response
        MatchLocation location1 = new MatchLocation("Location1");
        MatchLocation location2 = new MatchLocation("Location2");

        List<Match> mockMatches = List.of(
                new Match("Match1", "Description1", 50.0, MatchType.CHARITY, location1, 10),
                new Match("Match2", "Description2", 30.0, MatchType.COMPETITIVE, location2, 5)
        );

        when(matchService.listAllMatches()).thenReturn(mockMatches);

        mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("matches", mockMatches))
                .andExpect(model().attribute("totalFollows", 15)) // 10 + 5
                .andExpect(model().attribute("types", List.of(MatchType.values())));

        verify(matchService, times(1)).listAllMatches();
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShowMatches_WithPriceAndType() throws Exception {
        // Mocking service response for filtered matches
        MatchLocation location = new MatchLocation("Location1");

        List<Match> mockMatches = List.of(
                new Match("Match1", "Description1", 25.0, MatchType.CHARITY, location, 12)
        );

        when(matchService.listMatchesWithPriceLessThanAndType(30.0, MatchType.CHARITY)).thenReturn(mockMatches);

        mockMvc.perform(get("/matches")
                        .param("price", "30.0")
                        .param("type", "CHARITY"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("matches", mockMatches))
                .andExpect(model().attribute("totalFollows", 12))
                .andExpect(model().attribute("types", List.of(MatchType.values())));

        verify(matchService, times(1)).listMatchesWithPriceLessThanAndType(30.0, MatchType.CHARITY);
    }
}