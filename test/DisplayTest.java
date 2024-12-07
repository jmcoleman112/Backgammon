import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class DisplayTest {

    private Board testBoard;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        int[] red = {2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1};
        testBoard = new Board(red, blue);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testDisplayBoard() {
        Display.displayBoard(testBoard, 0, new Match());
        String output = outputStream.toString();
        assertTrue(output.contains("\u2554") && output.contains("\u255D"));
        assertTrue(output.contains("\033[0;31mO") || output.contains("\033[0;34mO"));
    }

    @Test
    public void testPrintPipNumbers() {
        Display.printPipNumbers(testBoard, true, 0);
        String output = outputStream.toString();
        assertTrue(output.contains("13"));
        assertTrue(output.contains("24"));
        assertTrue(output.contains("BAR"));
    }

    @Test
    public void testPrintDiceFace() {
        Display.printDiceFace(3, 5, false);
        String output = outputStream.toString();
        assertTrue(output.contains("┌───────┐") && output.contains("│   ●   │"));
    }

    @Test
    public void testPrintOverlay() {
        Match match = new Match();
        match.setScore(5, 0);
        match.setScore(4, 1);
        match.setMatchLength(7);
        match.updateDoubleCount();
        match.setDoubleOwner(0);

        Display.printOverlay(match, 0);
        String output = outputStream.toString();
        assertTrue(output.contains("\033[0;31m5"));
        assertTrue(output.contains("\033[0;34m4"));
    }

    @Test
    public void testPrintGameWinMessage() {
        Players players = new Players("Player 1", "Player 2");
        Match match = new Match();
        match.setScore(5, 0);

        int[] red = new int[26];
        int[] blue = new int[26];
        red[6] = 1;
        red[2] = 1;
        blue[22] = 2;
        blue[1] = 1;
        Board board = new Board(red, blue);

        Display.printGameWinMessage(players, 0, match, board);
        String output = outputStream.toString();
        assertTrue(output.contains("Player 1\033[0m" + "\033[32m Wins"));
        assertTrue(output.contains("5"));
    }

    @Test
    public void testPrintMatchWinMessage() {
        Players players = new Players("Player 1", "Player 2");
        Match match = new Match();
        match.setScore(9, 0);
        match.setScore(8, 1);

        Display.printMatchWinMessage(players, 0, match);
        String output = outputStream.toString();
        assertTrue(output.contains("Match Over!"));
        assertTrue(output.contains("\033[0;31mPlayer 1\033[0m\033[32m!"));
        assertTrue(output.contains("9"));
    }

    @Test
    public void testDisplayHint() {
        Display.displayHint(false, false, true);
        String output = outputStream.toString();
        assertTrue(output.contains("Enter 'roll' to roll dice"));
        assertTrue(output.contains("Enter 'board' to display game board"));
    }
}
