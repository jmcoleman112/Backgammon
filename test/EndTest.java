import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import static org.junit.Assert.*;

public class EndTest {

    private End end;

    @Before
    public void setUp() {
        end = new End(Colour.BLUE);
    }

    @Test
    public void testConstructor() {
        // Verify that the color is initialized correctly
        assertEquals("Expected color to be BLUE", Colour.BLUE, end.getColor());

        // Verify that the count is initialized to 0
        assertEquals("Expected count to be 0", 0, end.getCount());

        // Verify that the pipNumber is initialized to 0
        assertEquals("Expected pip number to be 0", 0, end.getPipNumber(0));
    }

    @Test
    public void testSetColor() {
        // Change the color and verify
        end.setColor(Colour.RED);
        assertEquals("Expected color to be RED", Colour.RED, end.getColor());
    }

    @Test
    public void testSetCount() {
        // Change the count and verify
        end.setCount(5);
        assertEquals("Expected count to be 5", 5, end.getCount());
    }

    @Test
    public void testGetPipNumberForPlayer0() {
        // Verify pip number for player 0 is always 0
        assertEquals("Expected pip number to be 0 for player 0", 0, end.getPipNumber(0));
    }

    @Test
    public void testGetPipNumberForPlayer1() {
        // Verify pip number for player 1 is always 0
        assertEquals("Expected pip number to be 0 for player 1", 0, end.getPipNumber(1));
    }

    @Test
    public void testGetPipCountForPlayer0() {
        // Verify pip count for player 0 is always 0 regardless of count
        end.setCount(3);
        assertEquals("Expected pip count to be 0 for player 0", 0, end.getPipCount(0));
    }

    @Test
    public void testGetPipCountForPlayer1() {
        // Verify pip count for player 1 is always 0 regardless of count
        end.setCount(3);
        assertEquals("Expected pip count to be 0 for player 1", 0, end.getPipCount(1));
    }
}
