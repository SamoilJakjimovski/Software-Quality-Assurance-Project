package mk.ukim.finki.wp.tests.graph;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import mk.ukim.finki.wp.tests.web.MatchesController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.service.MatchService;

import java.util.Collections;
import java.util.List;

public class ShowMatchesGraphTest {

    @Mock
    private MatchService matchService;

    @Mock
    private Model model;

    @InjectMocks
    private MatchesController matchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //    [1,2,4,5,6,7,10,11]
    @Test
    public void testShowMatchesWithPriceOrTypeNotNullAndEmptyMatches() {
        // Given
        Double price = 50.0;
        MatchType type = MatchType.COMPETITIVE;
        List<Match> matches = Collections.emptyList();
        when(matchService.listMatchesWithPriceLessThanAndType(price, type)).thenReturn(matches);

        // When
        String viewName = matchController.showMatches(price, type, model);

        // Then
        verify(matchService, times(1)).listMatchesWithPriceLessThanAndType(price, type);
        verify(model, times(1)).addAttribute("totalFollows", 0);
        verify(model, times(1)).addAttribute("matches", matches);
        verify(model, times(1)).addAttribute("types", List.of(MatchType.values()));
        assertEquals("list", viewName);
    }

    //    [1,2,3,5,6,7,10,11]
    @Test
    public void testShowMatchesPriceAndTypeNullAndEmptyMatches() {
        // Given
        Double price = null;
        MatchType type = null;
        List<Match> matches = Collections.emptyList();
        when(matchService.listAllMatches()).thenReturn(matches);

        // When
        String viewName = matchController.showMatches(price, type, model);

        // Then
        verify(matchService, times(1)).listAllMatches();
        verify(model, times(1)).addAttribute("totalFollows", 0);
        verify(model, times(1)).addAttribute("matches", matches);
        verify(model, times(1)).addAttribute("types", List.of(MatchType.values()));
        assertEquals("list", viewName);
    }

    //    [1,2,3,5,6,7,8,9,7,10,11]
    @Test
    public void testShowMatchesPriceAndTypeNull() {
        // Given
        Match match = new Match("Match1", "Description1", 10.0, MatchType.CHARITY, null, 10);
        List<Match> matches = List.of(match);
        when(matchService.listAllMatches()).thenReturn(matches);

        // When
        String viewName = matchController.showMatches(null, null, model);

        // Then
        verify(matchService, times(1)).listAllMatches();
        verify(model, times(1)).addAttribute("totalFollows", 10);
        verify(model, times(1)).addAttribute("matches", matches);
        verify(model, times(1)).addAttribute("types", List.of(MatchType.values()));
        assertEquals("list", viewName);
    }
}
