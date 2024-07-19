package mk.ukim.finki.wp.sep2022.unit;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import mk.ukim.finki.wp.sep2022.service.MatchLocationService;
import mk.ukim.finki.wp.sep2022.web.MatchesController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MatchesControllerTests {

    private MatchService matchService;
    private MatchLocationService locationService;
    private MatchesController matchesController;
    private Model model;

    @BeforeEach
    public void setup() {
        matchService = Mockito.mock(MatchService.class);
        locationService = Mockito.mock(MatchLocationService.class);
        model = Mockito.mock(Model.class);
        matchesController = new MatchesController(matchService, locationService);
    }

    @Test
    public void testShowMatchesAllNull() {
        List<Match> matches = List.of(new Match());
        when(matchService.listAllMatches()).thenReturn(matches);

        String view = matchesController.showMatches(null, null, model);
        assertEquals("list", view);
        verify(model).addAttribute("matches", matches);
        verify(model).addAttribute(eq("types"), anyList());
    }

    @Test
    public void testShowMatchesWithPrice() {
        List<Match> matches = List.of(new Match());
        when(matchService.listMatchesWithPriceLessThanAndType(100.0, null)).thenReturn(matches);

        String view = matchesController.showMatches(100.0, null, model);
        assertEquals("list", view);
        verify(model).addAttribute("matches", matches);
        verify(model).addAttribute(eq("types"), anyList());
    }

    @Test
    public void testShowMatchesWithType() {
        List<Match> matches = List.of(new Match());
        when(matchService.listMatchesWithPriceLessThanAndType(null, MatchType.FRIENDLY)).thenReturn(matches);

        String view = matchesController.showMatches(null, MatchType.FRIENDLY, model);
        assertEquals("list", view);
        verify(model).addAttribute("matches", matches);
        verify(model).addAttribute(eq("types"), anyList());
    }

    @Test
    public void testShowAdd() {
        List<MatchLocation> locations = List.of(new MatchLocation("Location 1"));
        when(locationService.listAll()).thenReturn(locations);

        String view = matchesController.showAdd(model);
        assertEquals("form", view);
        verify(model).addAttribute("locations", locations);
        verify(model).addAttribute(eq("types"), anyList());
    }

    @Test
    public void testShowEdit() {
        Match match = new Match();
        List<MatchLocation> locations = List.of(new MatchLocation("Location 1"));
        when(matchService.findById(1L)).thenReturn(match);
        when(locationService.listAll()).thenReturn(locations);

        String view = matchesController.showEdit(1L, model);
        assertEquals("form", view);
        verify(model).addAttribute("match", match);
        verify(model).addAttribute("locations", locations);
        verify(model).addAttribute(eq("types"), anyList());
    }

    @Test
    public void testCreate() {
        String view = matchesController.create("Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
        assertEquals("redirect:/matches", view);
        verify(matchService).create("Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
    }

    @Test
    public void testUpdate() {
        String view = matchesController.update(1L, "Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
        assertEquals("redirect:/matches", view);
        verify(matchService).update(1L, "Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
    }

    @Test
    public void testDelete() {
        String view = matchesController.delete(1L);
        assertEquals("redirect:/matches", view);
        verify(matchService).delete(1L);
    }

    @Test
    public void testFollow() {
        String view = matchesController.follow(1L);
        assertEquals("redirect:/matches", view);
        verify(matchService).follow(1L);
    }
}
