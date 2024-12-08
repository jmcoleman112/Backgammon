import org.junit.Before;
import org.junit.Test;
import Model.Bar;
import View.Colour;

import static org.junit.Assert.*;

public class BarTest {

    private Bar bar;

    @Before
    public void setUp() {
        bar = new Bar(Colour.RED);
    }

    @Test
    public void testGetPipCountForPlayer0() {
        assertEquals("Expected pip count to be 0 initially", 0, bar.getPipCount(0));
        bar.setCount(5);
        assertEquals("Expected pip count to be 125 (5 * 25)", 125, bar.getPipCount(0));
    }

    @Test
    public void testGetPipCountForPlayer1() {
        assertEquals("Expected pip count to be 0 initially", 0, bar.getPipCount(1));
        bar.setCount(3);
        assertEquals("Expected pip count to be 75 (3 * 25)", 75, bar.getPipCount(1));
    }

    @Test
    public void testConstructor() {
        assertEquals("Expected color to be RED", Colour.RED, bar.getColor());
        assertEquals("Expected pip number to be 25", 25, bar.getPipNumber(0));
        assertEquals("Expected count to be 0 initially", 0, bar.getCount());
    }

    @Test
    public void testSetColor() {
        bar.setColor(Colour.BLUE);
        assertEquals("Expected color to be BLUE", Colour.BLUE, bar.getColor());
    }

    @Test
    public void testSetCount() {
        bar.setCount(10);
        assertEquals("Expected count to be 10", 10, bar.getCount());
    }
}
