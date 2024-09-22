package mk.ukim.finki.wp.tests.repository;

import mk.ukim.finki.wp.tests.model.Match;
import mk.ukim.finki.wp.tests.model.MatchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @BeforeEach
    public void setUp() {
        // Add some test data
        matchRepository.save(new Match("Match1", "Exciting match", 25.0, MatchType.CHARITY, null));
        matchRepository.save(new Match("Match2", "Another match", 50.0, MatchType.FRIENDLY, null));
        matchRepository.save(new Match("Match3", "Interesting match", 30.0, MatchType.CHARITY, null));
    }

    @Test
    public void testFindAllByPriceLessThanEqual() {
        // Test finding matches with price less than or equal to 30.0
        List<Match> matches = matchRepository.findAllByPriceLessThanEqual(30.0);
        assertEquals(2, matches.size());
        assertEquals("Match1", matches.get(0).getName());
        assertEquals("Match3", matches.get(1).getName());
    }

    @Test
    public void testFindAllByTypeEquals() {
        // Test finding matches by type FOOTBALL
        List<Match> matches = matchRepository.findAllByTypeEquals(MatchType.CHARITY);
        assertEquals(2, matches.size());
        assertEquals(MatchType.CHARITY, matches.get(0).getType());
        assertEquals(MatchType.CHARITY, matches.get(1).getType());
    }

    @Test
    public void testFindAllByPriceLessThanEqualAndTypeEquals() {
        // Test finding matches with price <= 30.0 and type FOOTBALL
        List<Match> matches = matchRepository.findAllByPriceLessThanEqualAndTypeEquals(30.0, MatchType.CHARITY);
        assertEquals(2, matches.size());
        assertEquals("Match1", matches.get(0).getName());
        assertEquals("Match3", matches.get(1).getName());
    }
}
