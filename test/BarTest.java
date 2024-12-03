import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import static org.junit.Assert.*;

public class BarTest {

    private Bar bar;

    @Before
    public void setUp() {
        bar = new Bar(Colour.RED);
    }

    @Test
    public void testGetPipCountForPlayer0() {
        // Initially, the count is 0, so the pip count should be 0
        assertEquals("Expected pip count to be 0 initially", 0, bar.getPipCount(0));

        // Set count to 5 and verify the pip count
        bar.setCount(5);
        assertEquals("Expected pip count to be 125 (5 * 25)", 125, bar.getPipCount(0));
    }

    @Test
    public void testGetPipCountForPlayer1() {
        // Initially, the count is 0, so the pip count should be 0
        assertEquals("Expected pip count to be 0 initially", 0, bar.getPipCount(1));

        // Set count to 3 and verify the pip count
        bar.setCount(3);
        assertEquals("Expected pip count to be 75 (3 * 25)", 75, bar.getPipCount(1));
    }

    @Test
    public void testConstructor() {
        // Verify that the initial color is set correctly
        assertEquals("Expected color to be RED", Colour.RED, bar.getColor());

        // Verify that the initial pipNumber is set to 25
        assertEquals("Expected pip number to be 25", 25, bar.getPipNumber(0));

        // Verify that the initial count is 0
        assertEquals("Expected count to be 0 initially", 0, bar.getCount());
    }

    @Test
    public void testSetColor() {
        // Change the color and verify
        bar.setColor(Colour.BLUE);
        assertEquals("Expected color to be BLUE", Colour.BLUE, bar.getColor());
    }

    @Test
    public void testSetCount() {
        // Change the count and verify
        bar.setCount(10);
        assertEquals("Expected count to be 10", 10, bar.getCount());
    }
}
