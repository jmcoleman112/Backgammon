import org.junit.Before;
import org.junit.Test;
import utilities.Colour;
import utilities.Point;

import static org.junit.Assert.*;

public class PointTest {

    private Point point;

    @Before
    public void setUp() {
        point = new Point(Colour.RED, 5, 10);
    }

    @Test
    public void getColor() {
        assertEquals(Colour.RED, point.getColor());
    }

    @Test
    public void setColor() {
        point.setColor(Colour.BLUE);
        assertEquals(Colour.BLUE, point.getColor());
    }

    @Test
    public void getCount() {
        assertEquals(5, point.getCount());
    }

    @Test
    public void setCount() {
        point.setCount(3);
        assertEquals(3, point.getCount());
    }

    @Test
    public void getPipNumberForPlayer0() {
        assertEquals(10, point.getPipNumber(0));
    }

    @Test
    public void getPipNumberForPlayer1() {
        assertEquals(15, point.getPipNumber(1));
    }

    @Test
    public void getPipNumberForInvalidPlayer() {
        assertEquals(0, point.getPipNumber(2));
    }

    @Test
    public void getPipCountForPlayer0() {
        assertEquals(50, point.getPipCount(0));
    }

    @Test
    public void getPipCountForPlayer1() {
        assertEquals(0, point.getPipCount(1));
    }

    @Test
    public void getPipCountForInvalidPlayer() {
        assertEquals(0, point.getPipCount(2));
    }

    @Test
    public void getPipCountForPlayer2() {
        point.setColor(Colour.BLUE);
        assertEquals(75, point.getPipCount(1));
    }
}
