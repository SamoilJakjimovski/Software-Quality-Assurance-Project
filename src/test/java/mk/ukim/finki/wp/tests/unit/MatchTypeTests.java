package mk.ukim.finki.wp.tests.unit;

import mk.ukim.finki.wp.tests.model.MatchType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MatchTypeTests {

    @Test
    public void testGetAuthority() {
        assertEquals("FRIENDLY", MatchType.FRIENDLY.getAuthority());
        assertEquals("COMPETITIVE", MatchType.COMPETITIVE.getAuthority());
        assertEquals("CHARITY", MatchType.CHARITY.getAuthority());
    }
}
