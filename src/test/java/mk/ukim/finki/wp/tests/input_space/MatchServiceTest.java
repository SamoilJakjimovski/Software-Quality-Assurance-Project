package mk.ukim.finki.wp.tests.input_space;

import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.repository.MatchLocationRepository;
import mk.ukim.finki.wp.tests.repository.MatchRepository;
import mk.ukim.finki.wp.tests.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchLocationRepository matchLocationRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    // Valid Input Parameters (Happy Path)
    @Test
    public void testCreateValidMatch() {
        String name = "Friendly Match";
        String description = "A friendly match.";
        Double price = 30.0;
        MatchType type = MatchType.FRIENDLY;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        Match expectedMatch = new Match(name, description, price, type, location);

//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

        Match actualMatch = matchService.create(name, description, price, type, locationId);

        assertNotNull(actualMatch);
        assertEquals(expectedMatch, actualMatch);
    }

    // Null Name -> match won't be saved
    @Test
    public void testCreateMatchWithNullName() {
        String description = "A charity match.";
        Double price = 20.0;
        MatchType type = MatchType.CHARITY;
        Long locationId = 1L;

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.create(null, description, price, type, locationId);
        });

        String expectedMessage = "Invalid argument";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Null Description -> match won't be saved
    @Test
    public void testCreateMatchWithNullDescription() {
        String name = "Friendly Match";
        Double price = 25.0;
        MatchType type = MatchType.FRIENDLY;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.create(name, null, price, type, locationId);
        });

        String expectedMessage = "Invalid argument";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    // Invalid Price (Negative Value) -> match won't be saved
    @Test
    public void testCreateMatchWithNegativePrice() {
        String name = "Competitive Match";
        String description = "A competitive match.";
        Double price = -50.0; // Invalid price
        MatchType type = MatchType.COMPETITIVE;
        Long locationId = 1L;

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.create(name, description, price, type, locationId);
        });

        String expectedMessage = "Invalid argument";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Null Type -> match won't be saved
    @Test
    public void testCreateMatchWithNullType() {
        String name = "Charity Match";
        String description = "A charity match.";
        Double price = 10.0;
        Long locationId = 1L;

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.create(name, description, price, null, locationId);
        });

        String expectedMessage = "Invalid argument";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    // Null Location -> match won't be saved
    @Test
    public void testCreateMatchWithNullLocation() {
        String name = "Charity Match";
        String description = "A charity match.";
        Double price = 10.0;
        MatchType type = MatchType.CHARITY;
        Long locationId = null;

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matchService.create(name, description, price, type, locationId);
        });

        String expectedMessage = "Invalid argument";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
