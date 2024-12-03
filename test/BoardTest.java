import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(); // Default board setup
    }

    @Test
    public void getPoint() {
        assertNotNull(board.getPoint(0));
        assertEquals(Colour.BLUE, board.getPoint(0).getColor());
        assertEquals(2, board.getPoint(0).getCount());
    }

    @Test
    public void getPointCount() {
        assertEquals(2, board.getPointCount(0));
        assertEquals(0, board.getPointCount(1));
    }

    @Test
    public void getPointColor() {
        assertEquals(Colour.BLUE, board.getPointColor(0));
        assertEquals(Colour.NONE, board.getPointColor(1));
    }

    @Test
    public void getBar() {
        assertNotNull(board.getBar(0));
        assertEquals(Colour.RED, board.getBar(0).getColor());
        assertEquals(0, board.getBar(0).getCount());
    }

    @Test
    public void getEnd() {
        assertNotNull(board.getEnd(0));
        assertEquals(Colour.RED, board.getEnd(0).getColor());
        assertEquals(0, board.getEnd(0).getCount());
    }

    @Test
    public void getPlayerColor() {
        assertEquals(Colour.RED, board.getPlayerColor(0));
        assertEquals(Colour.BLUE, board.getPlayerColor(1));
    }

    @Test
    public void bearoffcheck() {
        int[] red = new int[26];
        int[] blue = new int[26];
        red[0] = 5;
        red[1] = 5;
        red[2] = 5; // Red has 15 checkers in its home board
        Board testBoard = new Board(red, blue);
        assertTrue(testBoard.bearoffcheck(0));
        assertFalse(board.bearoffcheck(0)); // Default board should fail bearoffcheck
    }

    @Test
    public void maxPoint() {
        assertEquals(5, board.maxPoint()); // Default board max point
    }

    @Test
    public void getTotalPipCount() {
        int[] red = new int[26];
        red[0] = 2; // Two checkers on 1-point
        red[5] = 5; // Five checkers on 6-point
        red[24] = 3; // Three checkers on the bar
        Board testBoard = new Board(red, new int[26]);
        assertEquals(25*3 + (1 * 2) + (6 * 5), testBoard.getTotalPipCount(0));
    }

    @Test
    public void getColoredPoints() {
        List<Integer> redPoints = board.getColoredPoints(Colour.RED);
        assertTrue(redPoints.contains(5));
        assertTrue(redPoints.contains(12));
        assertFalse(redPoints.contains(0)); // Blue point
    }

    @Test
    public void noGameWinner() {
        assertTrue(board.noGameWinner()); // Default board has no winner
        board.setWinner(0);

        int[] red = new int[26];
        int[] blue = new int[26];
        red[0] = 5;
        Board winBoard = new Board(red, blue);
        assertFalse(winBoard.noGameWinner());
    }

    @Test
    public void getWinner() {
        assertEquals(-1, board.getWinner()); // Default board has no winner
        board.setWinner(0);
        assertEquals(0, board.getWinner());
    }

    @Test
    public void setWinner() {
        board.setWinner(1);
        assertEquals(1, board.getWinner());
    }
}
