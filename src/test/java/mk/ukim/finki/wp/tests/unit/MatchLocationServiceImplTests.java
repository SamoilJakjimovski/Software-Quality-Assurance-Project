package mk.ukim.finki.wp.tests.unit;

import mk.ukim.finki.wp.tests.model.MatchLocation;
import mk.ukim.finki.wp.tests.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.tests.repository.MatchLocationRepository;
import mk.ukim.finki.wp.tests.service.impl.MatchLocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

public class MatchLocationServiceImplTests {

    private MatchLocationRepository matchLocationRepository;
    private MatchLocationServiceImpl matchLocationService;

    @BeforeEach
    public void setup() {
        matchLocationRepository = Mockito.mock(MatchLocationRepository.class);
        matchLocationService = new MatchLocationServiceImpl(matchLocationRepository);
    }

    @Test
    public void testFindByIdValidId() {
        MatchLocation location = new MatchLocation("Location 1");
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        MatchLocation result = matchLocationService.findById(1L);
        assertEquals(location, result);
    }

    @Test
    public void testFindByIdInvalidId() {
        Mockito.when(matchLocationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(InvalidMatchLocationIdException.class, () -> matchLocationService.findById(1L));
    }

    @Test
    public void testListAll() {
        matchLocationService.listAll();
        Mockito.verify(matchLocationRepository).findAll();
    }

    @Test
    public void testCreateValidName() {
        MatchLocation location = new MatchLocation("Location 1");
        Mockito.when(matchLocationRepository.save(Mockito.any(MatchLocation.class))).thenReturn(location);

        MatchLocation result = matchLocationService.create("Location 1");
        assertEquals(location, result);
    }
}
