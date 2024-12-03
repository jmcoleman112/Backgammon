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
        // Define a custom board setup
        int[] red = {2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}; // Red stones at specific positions
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1}; // Blue stones at specific positions
        testBoard = new Board(red, blue);

        // Redirect system output to capture printed content
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testDisplayBoard() {
        Display.displayBoard(testBoard, 0, new Match());

        String output = outputStream.toString();
        assertTrue("Output should contain board corners", output.contains("\u2554") && output.contains("\u255D"));
        assertTrue("Output should display points for red and blue pieces", output.contains("\033[0;31mO") || output.contains("\033[0;34mO"));
    }

    @Test
    public void testPrintPipNumbers() {
        Display.printPipNumbers(testBoard, true, 0);

        String output = outputStream.toString();
        assertTrue("Output should contain first pip number", output.contains("13")); // First pip number
        assertTrue("Output should contain last pip number", output.contains("24")); // Last pip number
        assertTrue("Output should contain BAR", output.contains("BAR")); // BAR
    }

    @Test
    public void testPrintDiceFace() {
        Display.printDiceFace(3, 5, false);

        String output = outputStream.toString();
        assertTrue("Output should contain dice faces", output.contains("┌───────┐") && output.contains("│   ●   │"));
    }

    @Test
    public void testPrintOverlay() {
        Match match = new Match();
        match.setScore(5, 0);
        match.setScore(4, 1);
        match.setMatchLength(7);
        match.updateDoubleCount(); //Goes form 1 to 2
        match.setDoubleOwner(0);

        Display.printOverlay(match, 0);

        String output = outputStream.toString();
        assertTrue("Output should display red score", output.contains("\033[0;31m5"));
        assertTrue("Output should display blue score", output.contains("\033[0;34m4"));
    }

    @Test
    public void testPrintGameWinMessage() {
        Players players = new Players("Player 1", "Player 2");
        Match match = new Match();
        match.setScore(5, 0);

        Display.printGameWinMessage(players, 0, match);

        String output = outputStream.toString();
        assertTrue("Output should display the winner's name", output.contains("\033[0;31mPlayer 1\033[0m Wins!"));
        assertTrue("Output should display the final score", output.contains("5"));
    }

    @Test
    public void testPrintMatchWinMessage() {
        Players players = new Players("Player 1", "Player 2");
        Match match = new Match();
        match.setScore(9, 0);
        match.setScore(8, 1);

        Display.printMatchWinMessage(players, 0, match);

        String output = outputStream.toString();
        assertTrue("Output should display match end message", output.contains("Game Over"));
        assertTrue("Output should display the winner's name", output.contains("\033[0;31mPlayer 1\033[0m!"));
        assertTrue("Output should display the final score", output.contains("9"));
    }

    @Test
    public void testDisplayHint() {
        Display.displayHint(false, true);

        String output = outputStream.toString();
        assertTrue("Output should include the 'roll' command", output.contains("Enter 'roll' to roll dice"));
        assertTrue("Output should include the 'board' command", output.contains("Enter 'board' to display game board"));
    }
}
