package mk.ukim.finki.wp.tests.unit;

import mk.ukim.finki.wp.tests.model.MatchLocation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchLocationTests {

    @Test
    public void testMatchLocationConstructor() {
        MatchLocation location = new MatchLocation("Location 1");

        assertEquals("Location 1", location.getName());
    }

    @Test
    public void testMatchLocationSettersAndGetters() {
        MatchLocation location = new MatchLocation();
        location.setName("Location 1");

        assertEquals("Location 1", location.getName());
    }
}

