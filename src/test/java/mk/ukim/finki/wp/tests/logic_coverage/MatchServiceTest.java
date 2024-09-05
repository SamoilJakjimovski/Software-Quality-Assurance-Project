package mk.ukim.finki.wp.tests.logic_coverage;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    // (match.getFollows() >= 0 && match.getFollows() <= 25) || !match.getType().equals(MatchType.UNASSIGNED);
    //A = match.getFollows() >= 0
    //B = match.getFollows() <= 25
    //C = !match.getType().equals(MatchType.UNASSIGNED);

    //Major Clause	Set of possible tests RACC
    //
    //a	(1,5)
    //b	(1,3)
    //c	(3,4), (5,6), (7,8)

    //    Tests: (1, 5, 3, 4)
    //Row#	a	b	c		P		Pa	Pb	Pc
    //1		T	T	T		T		T	T
    //2		T	T	 		T
    //3		T	 	T		 		 	T	T
    //4		T	 	 		T		 	 	T
    //5		 	T	T		 		T	 	T
    //6		 	T	 		T		 	 	T
    //7		 	 	T		 		 	 	T
    //8		 	 	 		T		 	 	T

    //1
    @Test
    public void createMatchWhereEachClauseIsTrue() {
        String name = "Friendly Match";
        String description = "A friendly match.";
        Double price = 30.0;
        MatchType type = MatchType.UNASSIGNED;
        Integer follows = 10;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        Match expectedMatch = new Match(name, description, price, type, location, follows);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);
        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

        boolean shouldReturnTrue = matchService.canFollow(expectedMatch);
        assertTrue(shouldReturnTrue);
    }

    //5
    @Test
    public void createMatchWhereFirstClauseIsFalse() {
        String name = "Friendly Match";
        String description = "A friendly match.";
        Double price = 30.0;
        MatchType type = MatchType.UNASSIGNED;
        Integer follows = 30;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        Match expectedMatch = new Match(name, description, price, type, location, follows);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);
        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

        boolean shouldReturnFalse = matchService.canFollow(expectedMatch);
        assertFalse(shouldReturnFalse);
    }

    //3
    @Test
    public void createMatchWhereSecondClauseIsFalse() {
        String name = "Friendly Match";
        String description = "A friendly match.";
        Double price = 30.0;
        MatchType type = MatchType.UNASSIGNED;
        Integer follows = 30;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        Match expectedMatch = new Match(name, description, price, type, location, follows);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);
        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

        boolean shouldReturnFalse = matchService.canFollow(expectedMatch);
        assertFalse(shouldReturnFalse);
    }

    //4
    @Test
    public void createMatchWhereSecondAndThirdClauseIsFalse() {
        String name = "Friendly Match";
        String description = "A friendly match.";
        Double price = 30.0;
        MatchType type = MatchType.COMPETITIVE;
        Integer follows = 10;
        Long locationId = 1L;

        MatchLocation location = new MatchLocation("Stadium");
        location.setId(locationId);

        Match expectedMatch = new Match(name, description, price, type, location, follows);

        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);
        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);

        boolean shouldReturnTrue = matchService.canFollow(expectedMatch);
        assertTrue(shouldReturnTrue);
    }





//    // Case 1: T, T (price != null, price > 0.0)
//    @Test
//    public void testCreateMatchValidPrice() {
//        String name = "Friendly Match";
//        String description = "A friendly match.";
//        Double price = 30.0; // Valid price
//        MatchType type = MatchType.FRIENDLY;
//        Long locationId = 1L;
//
//        MatchLocation location = new MatchLocation("Stadium");
//        location.setId(locationId);
//
//        Match expectedMatch = new Match(name, description, price, type, location);
//
//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
////        when(matchRepository.save(expectedMatch)).thenReturn(expectedMatch);
//        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);
//
//        Match actualMatch = matchService.create(name, description, price, type, locationId);
//
//        assertNotNull(actualMatch);
//        assertEquals(expectedMatch, actualMatch);
//    }
//
//    // Case 2: F, T (price == null, price > 0.0)
//    @Test
//    public void testCreateMatchNullPrice() {
//        String name = "Friendly Match";
//        String description = "A friendly match.";
//        Double price = null; // Null price
//        MatchType type = MatchType.FRIENDLY;
//        Long locationId = 1L;
//
//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            matchService.create(name, description, price, type, locationId);
//        });
//
//        String expectedMessage = "Invalid argument";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    // Case 3: T, F (price != null, price <= 0.0)
//    @Test
//    public void testCreateMatchInvalidPrice() {
//        String name = "Friendly Match";
//        String description = "A friendly match.";
//        Double price = -10.0; // Invalid price
//        MatchType type = MatchType.FRIENDLY;
//        Long locationId = 1L;
//
//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(new MatchLocation("Stadium")));
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            matchService.create(name, description, price, type, locationId);
//        });
//
//        String expectedMessage = "Invalid argument";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    // Predicate: location != null
//    // Clauses:
//    // c - location != null
//
//    // Case 4: T (location != null)
//    @Test
//    public void testCreateMatchValidLocation() {
//        String name = "Friendly Match";
//        String description = "A friendly match.";
//        Double price = 30.0;
//        MatchType type = MatchType.FRIENDLY;
//        Long locationId = 1L;
//
//        MatchLocation location = new MatchLocation("Stadium");
//        location.setId(locationId);
//
//        Match expectedMatch = new Match(name, description, price, type, location);
//
//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.of(location));
//        when(matchRepository.save(any(Match.class))).thenReturn(expectedMatch);
//
//        Match actualMatch = matchService.create(name, description, price, type, locationId);
//
//        assertNotNull(actualMatch);
//        assertEquals(expectedMatch, actualMatch);
//    }
//
//    // Case 5: F (location == null)
//    @Test
//    public void testCreateMatchInvalidLocation() {
//        String name = "Friendly Match";
//        String description = "A friendly match.";
//        Double price = 30.0;
//        MatchType type = MatchType.FRIENDLY;
//        Long locationId = 1L;
//
//        when(matchLocationRepository.findById(locationId)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(InvalidMatchLocationIdException.class, () -> {
//            matchService.create(name, description, price, type, locationId);
//        });
//
//        String expectedMessage = "Invalid locationId";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
}
