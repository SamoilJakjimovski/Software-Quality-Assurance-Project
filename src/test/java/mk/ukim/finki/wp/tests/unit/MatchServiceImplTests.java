package mk.ukim.finki.wp.tests.unit;

import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.MatchType;
import mk.ukim.finki.wp.tests.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.tests.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.tests.repository.MatchLocationRepository;
import mk.ukim.finki.wp.tests.repository.MatchRepository;
import mk.ukim.finki.wp.tests.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

public class MatchServiceImplTests {

    private MatchRepository matchRepository;
    private MatchLocationRepository matchLocationRepository;
    private PasswordEncoder passwordEncoder;
    private MatchServiceImpl matchService;

    @BeforeEach
    public void setup() {
        matchRepository = Mockito.mock(MatchRepository.class);
        matchLocationRepository = Mockito.mock(MatchLocationRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        matchService = new MatchServiceImpl(matchRepository, matchLocationRepository, passwordEncoder);
    }

    @Test
    public void testFindByIdValidId() {
        Match match = new Match();
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));

        Match result = matchService.findById(1L);
        assertEquals(match, result);
    }

    @Test
    public void testFindByIdInvalidId() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchIdException.class, () -> matchService.findById(1L));
    }

    @Test
    public void testCreateValidData() {
        MatchLocation location = new MatchLocation("Location 1");
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Match match = new Match("Match 1", "Description", 100.0, MatchType.FRIENDLY, location);
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);

        Match result = matchService.create("Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
        assertEquals(match, result);
    }

    @Test
    public void testCreateInvalidMatchLocationId() {
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchLocationIdException.class, () -> matchService.create("Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L));
    }

    @Test
    public void testUpdateValidData() {
        Match match = new Match();
        MatchLocation location = new MatchLocation("Location 1");
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);

        Match result = matchService.update(1L, "Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L);
        assertEquals(match, result);
    }

    @Test
    public void testUpdateInvalidMatchId() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchIdException.class, () -> matchService.update(1L, "Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L));
    }

    @Test
    public void testUpdateInvalidMatchLocationId() {
        Match match = new Match();
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchLocationIdException.class, () -> matchService.update(1L, "Match 1", "Description", 100.0, MatchType.FRIENDLY, 1L));
    }

    @Test
    public void testDeleteValidId() {
        Match match = new Match();
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));

        Match result = matchService.delete(1L);
        assertEquals(match, result);
        Mockito.verify(matchRepository).delete(match);
    }

    @Test
    public void testDeleteInvalidId() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchIdException.class, () -> matchService.delete(1L));
    }

    @Test
    public void testFollowValidId() {
        Match match = new Match();
        match.setFollows(0);
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        Mockito.when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);

        Match result = matchService.follow(1L);
        assertEquals(1, result.getFollows());
    }

    @Test
    public void testFollowInvalidId() {
        Mockito.when(matchRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchIdException.class, () -> matchService.follow(1L));
    }
}
