package mk.ukim.finki.wp.sep2022.unit;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTests {

    @Test
    public void testMatchConstructor() {
        MatchLocation location = new MatchLocation("Location 1");
        Match match = new Match("Match 1", "Description", 100.0, MatchType.FRIENDLY, location);

        assertEquals("Match 1", match.getName());
        assertEquals("Description", match.getDescription());
        assertEquals(100.0, match.getPrice());
        assertEquals(MatchType.FRIENDLY, match.getType());
        assertEquals(location, match.getLocation());
        assertEquals(0, match.getFollows());
    }

    @Test
    public void testMatchSettersAndGetters() {
        Match match = new Match();
        match.setName("Match 1");
        match.setDescription("Description");
        match.setPrice(100.0);
        match.setType(MatchType.FRIENDLY);
        MatchLocation location = new MatchLocation("Location 1");
        match.setLocation(location);
        match.setFollows(10);

        assertEquals("Match 1", match.getName());
        assertEquals("Description", match.getDescription());
        assertEquals(100.0, match.getPrice());
        assertEquals(MatchType.FRIENDLY, match.getType());
        assertEquals(location, match.getLocation());
        assertEquals(10, match.getFollows());
    }
}
