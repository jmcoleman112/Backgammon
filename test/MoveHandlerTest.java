import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import static org.junit.Assert.*;

public class MoveHandlerTest {

    private MoveHandler moveHandler;
    private Board testBoard;

    @Before
    public void setUp() {
        // Define a custom board setup
        int[] red = {2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1};
        testBoard = new Board(red, blue);
        moveHandler = new MoveHandler(testBoard);
    }

    @Test
    public void testLegalMovesWithBar() {
        // Player 0 (RED) has checkers on the bar
        testBoard.getBar(0).setCount(1);

        boolean result = moveHandler.legalmoves(0, 3, 4);

        assertTrue("Player 0 should have legal moves when they have checkers on the bar", result);
    }

    @Test
    public void testLegalMovesWithoutBar() {
        // Ensure no checkers on the bar
        testBoard.getBar(0).setCount(0);

        boolean result = moveHandler.legalmoves(0, 3, 4);

        assertTrue("Player 0 should have legal moves when no checkers are on the bar", result);
    }

    @Test
    public void testNoLegalMoves() {
        // Set up the board so that no moves are possible
        int[] red = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        testBoard = new Board(red, blue);
        moveHandler.setBoard(testBoard);

        boolean result = moveHandler.legalmoves(0, 3, 4);

        assertFalse("There should be no legal moves available for player 0", result);
    }

    @Test
    public void testExecuteMove() {
        // Player 0 moves from point 0 to point 3
        moveHandler.executeMove(0, 3);

        assertEquals("Point 0 should have one less checker", 1, testBoard.getPoint(0).getCount());
        assertEquals("Point 3 should have one more checker", 1, testBoard.getPoint(3).getCount());
        assertEquals("Point 3 should be RED", Colour.RED, testBoard.getPoint(3).getColor());
    }

    @Test
    public void testExecuteMoveWithPipping() {
        // Player 0 moves from point 0 to point 9, pipping Player 1
        testBoard.getPoint(9).setColor(Colour.BLUE);
        testBoard.getPoint(9).setCount(1);

        moveHandler.executeMove(0, 9);

        assertEquals("Point 0 should have one less checker", 1, testBoard.getPoint(0).getCount());
        assertEquals("Point 9 should have one RED checker", 1, testBoard.getPoint(9).getCount());
        assertEquals("Point 9 should be RED", Colour.RED, testBoard.getPoint(9).getColor());
        assertEquals("Player 1's bar should have one checker", 1, testBoard.getBar(1).getCount());
    }

    @Test
    public void testExecuteMoveToEmptyPoint() {
        // Player 0 moves from point 0 to an empty point
        moveHandler.executeMove(0, 3);

        assertEquals("Point 0 should have one less checker", 1, testBoard.getPoint(0).getCount());
        assertEquals("Point 3 should have one RED checker", 1, testBoard.getPoint(3).getCount());
        assertEquals("Point 3 should be RED", Colour.RED, testBoard.getPoint(3).getColor());
    }

    @Test
    public void testExecuteMoveToEnd() {
        // Player 0 bears off from point 23
        testBoard.getPoint(23).setCount(1);
        testBoard.getPoint(23).setColor(Colour.RED);

        moveHandler.executeMove(23, -1);

        assertEquals("Point 23 should have no checkers", 0, testBoard.getPoint(23).getCount());
        assertEquals("Point 23 should be empty", Colour.NONE, testBoard.getPoint(23).getColor());
        assertEquals("Player 0's end should have one checker", 1, testBoard.getEnd(0).getCount());
    }

    @Test
    public void testGetMoveFromCommand() {
        // Mock available moves
        moveHandler.legalmoves(0, 3, 4);

        int[] move = moveHandler.getMoveFromCommand("a");
        assertNotNull("Move 'a' should exist", move);
    }

    @Test
    public void testIsValidMoveCommand() {
        // Mock available moves
        moveHandler.legalmoves(0, 3, 4);

        assertTrue("Move 'a' should be valid", moveHandler.isValidMoveCommand("a"));
        assertFalse("Move 'z' should not be valid", moveHandler.isValidMoveCommand("z"));
    }
}
