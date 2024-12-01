import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import static org.junit.Assert.*;

public class PointTest {

    private Point point;

    @Before
    public void setUp() {
        point = new Point(Colour.RED, 5, 10);
    }

    @Test
    public void getColor() {
        assertEquals("Expected color to be RED", Colour.RED, point.getColor());
    }

    @Test
    public void setColor() {
        point.setColor(Colour.BLUE);
        assertEquals("Expected color to be BLUE", Colour.BLUE, point.getColor());
    }

    @Test
    public void getCount() {
        assertEquals("Expected count to be 5", 5, point.getCount());
    }

    @Test
    public void setCount() {
        point.setCount(3);
        assertEquals("Expected count to be 3", 3, point.getCount());
    }

    @Test
    public void getPipNumberForPlayer0() {
        assertEquals("Expected pip number for player 0 to be 10", 10, point.getPipNumber(0));
    }

    @Test
    public void getPipNumberForPlayer1() {
        assertEquals("Expected pip number for player 1 to be 15 (25 - 10)", 15, point.getPipNumber(1));
    }

    @Test
    public void getPipNumberForInvalidPlayer() {
        assertEquals("Expected pip number for invalid player to be 0", 0, point.getPipNumber(2));
    }

    @Test
    public void getPipCountForPlayer0() {
        // RED player owns the point
        assertEquals("Expected pip count for RED player 0 to be 50 (10 * 5)", 50, point.getPipCount(0));
    }

    @Test
    public void getPipCountForPlayer1() {
        // BLUE player has no pips on a RED point
        assertEquals("Expected pip count for BLUE player 1 to be 0", 0, point.getPipCount(1));
    }

    @Test
    public void getPipCountForInvalidPlayer() {
        assertEquals("Expected pip count for invalid player to be 0", 0, point.getPipCount(2));
    }

    @Test
    public void getPipCountForPlayer2() {
        // Change point color to BLUE and test pip count for player 1
        point.setColor(Colour.BLUE);
        assertEquals("Expected pip count for BLUE player 1 to be 75 (15 * 5)", 75, point.getPipCount(1));
    }
}
