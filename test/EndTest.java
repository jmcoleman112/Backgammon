import org.junit.Before;
import org.junit.Test;
import View.Colour;
import Model.End;

import static org.junit.Assert.*;

public class EndTest {

    private End end;

    @Before
    public void setUp() {
        end = new End(Colour.BLUE);
    }

    @Test
    public void testConstructor() {
        assertEquals(Colour.BLUE, end.getColor());
        assertEquals(0, end.getCount());
        assertEquals(0, end.getPipNumber(0));
    }

    @Test
    public void testSetColor() {
        end.setColor(Colour.RED);
        assertEquals(Colour.RED, end.getColor());
    }

    @Test
    public void testSetCount() {
        end.setCount(5);
        assertEquals(5, end.getCount());
    }

    @Test
    public void testGetPipNumberForPlayer0() {
        assertEquals(0, end.getPipNumber(0));
    }

    @Test
    public void testGetPipNumberForPlayer1() {
        assertEquals(0, end.getPipNumber(1));
    }

    @Test
    public void testGetPipCountForPlayer0() {
        end.setCount(3);
        assertEquals(0, end.getPipCount(0));
    }

    @Test
    public void testGetPipCountForPlayer1() {
        end.setCount(3);
        assertEquals(0, end.getPipCount(1));
    }
}
