package mk.ukim.finki.wp.sep2022.unit;

import mk.ukim.finki.wp.sep2022.model.MatchType;
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
