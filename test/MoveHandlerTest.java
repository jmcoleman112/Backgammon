import org.junit.Before;
import org.junit.Test;
import utilities.Board;
import utilities.Colour;
import utilities.MoveHandler;

import static org.junit.Assert.*;

public class MoveHandlerTest {

    private MoveHandler moveHandler;
    private Board testBoard;

    @Before
    public void setUp() {
        int[] red = {2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1};
        testBoard = new Board(red, blue);
        moveHandler = new MoveHandler(testBoard);
    }

    @Test
    public void testLegalMovesWithBar() {
        testBoard.getBar(0).setCount(1);
        boolean result = moveHandler.legalmoves(0, 3, 4);
        assertTrue(result);
    }

    @Test
    public void testLegalMovesWithoutBar() {
        testBoard.getBar(0).setCount(0);
        boolean result = moveHandler.legalmoves(0, 3, 4);
        assertTrue(result);
    }

    @Test
    public void testNoLegalMoves() {
        int[] red = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        testBoard = new Board(red, blue);
        moveHandler.setBoard(testBoard);
        boolean result = moveHandler.legalmoves(0, 3, 4);
        assertFalse(result);
    }

    @Test
    public void testExecuteMove() {
        moveHandler.executeMove(0, 3);
        assertEquals(1, testBoard.getPoint(0).getCount());
        assertEquals(1, testBoard.getPoint(3).getCount());
        assertEquals(Colour.RED, testBoard.getPoint(3).getColor());
    }

    @Test
    public void testExecuteMoveWithPipping() {
        testBoard.getPoint(9).setColor(Colour.BLUE);
        testBoard.getPoint(9).setCount(1);
        moveHandler.executeMove(0, 9);
        assertEquals(1, testBoard.getPoint(0).getCount());
        assertEquals(1, testBoard.getPoint(9).getCount());
        assertEquals(Colour.RED, testBoard.getPoint(9).getColor());
        assertEquals(1, testBoard.getBar(1).getCount());
    }

    @Test
    public void testExecuteMoveToEmptyPoint() {
        moveHandler.executeMove(0, 3);
        assertEquals(1, testBoard.getPoint(0).getCount());
        assertEquals(1, testBoard.getPoint(3).getCount());
        assertEquals(Colour.RED, testBoard.getPoint(3).getColor());
    }

    @Test
    public void testExecuteMoveToEnd() {
        testBoard.getPoint(23).setCount(1);
        testBoard.getPoint(23).setColor(Colour.RED);
        moveHandler.executeMove(23, -1);
        assertEquals(0, testBoard.getPoint(23).getCount());
        assertEquals(Colour.NONE, testBoard.getPoint(23).getColor());
        assertEquals(1, testBoard.getEnd(0).getCount());
    }

    @Test
    public void testGetMoveFromCommand() {
        moveHandler.legalmoves(0, 3, 4);
        int[] move = moveHandler.getMoveFromCommand("a");
        assertNotNull(move);
    }

    @Test
    public void testIsValidMoveCommand() {
        moveHandler.legalmoves(0, 3, 4);
        assertTrue(moveHandler.isValidMoveCommand("a"));
        assertFalse(moveHandler.isValidMoveCommand("z"));
    }
}
