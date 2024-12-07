import org.junit.Before;
import org.junit.Test;
import utilities.Board;
import utilities.Colour;
import utilities.InputHandler;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class BackgammonTest {
    private Backgammon game;

    @Before
    public void setUp() {
        game = new Backgammon();
        setupInput("Alice\nBob\n");
        game.setPlayers("", "", game.getInputHandler());
    }

    public ByteArrayOutputStream setupOutput() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        return outputStream;
    }

    public void setupInput(String input) {
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));
        game.setInputHandler(testInputHandler);
    }

    @Test
    public void testSetPlayers() {
        setupInput("Alice\nBob\n");
        game.setPlayers("", "", game.getInputHandler());
        assertEquals("\033[0;31mAlice\033[0m", game.getPlayers().getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", game.getPlayers().getPlayerName(1));
    }

    @Test
    public void testSetPlayerStrings() {
        String name1 = "Alice";
        String name2 = "Bob";
        game.setPlayers(name1, name2, new InputHandler());
        assertEquals("\033[0;31mAlice\033[0m", game.getPlayers().getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", game.getPlayers().getPlayerName(1));
    }

    @Test
    public void testGetInputHandler() {
        Backgammon game = new Backgammon();
        InputHandler inputHandler = game.getInputHandler();
        assertNotNull("utilities.InputHandler should not be null", inputHandler);
    }

    @Test
    public void testWelcomeFile() {
        setupInput("test filename\n");
        assertEquals(game.Welcome(), "filename");
    }

    @Test
    public void testWelcomeNoFile() {
        setupInput("\n");
        assertNull(game.Welcome());
    }

    @Test
    public void testRollTurn() {
        int[] rolls = game.rollTurn(0);
        assertTrue(rolls[0] >= 1 && rolls[0] <= 6);
        assertTrue(rolls[1] >= 1 && rolls[1] <= 6);
    }

    @Test
    public void testDecideFirstPlayer() {
        setupInput("roll 3\nroll 4\na\n");
        int firstPlayer = game.decideFirstPlayer();
        assertEquals(0, firstPlayer);
    }

    @Test
    public void testProcessTurnValidMove() {
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        setupInput("a\n");
        boolean turnInProgress = game.processTurn(0, "roll 4 6", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoubles() {
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        setupInput("a\na\n");
        boolean turnInProgress = game.processTurn(0, "roll 6 6", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoublesNoSecondMove() {
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[18] = 2;
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        setupInput("a\na\n");
        boolean turnInProgress = game.processTurn(0, "roll 3 3", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoublesWinner() {
        int[] red = new int[26];
        red[5] = 2;
        int[] blue = new int[26];
        blue[18] = 2;
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        setupInput("a\na\n");
        boolean turnInProgress = game.processTurn(0, "roll 6 6", null);
        assertFalse(turnInProgress);
        assertEquals(game.getBoard().getWinner(), 0);
    }

    @Test
    public void testProcessTurnInvalidMove() {
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        boolean turnInProgress = game.processTurn(0, "invalid", null);
        assertTrue(turnInProgress);
    }

    @Test
    public void testProcessTurnNoMoves() {
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[23] = 2;
        blue[22] = 2;
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        boolean turnInProgress = game.processTurn(0, "roll 1 2", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnSetBoard() {
        game.processTurn(0, "setboard 0N 0N 0N 0N 0N 1B 2R 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 2B 2B 0N", null);
        assertEquals(game.getBoard().getPoint(5).getCount(), 1);
        assertEquals(game.getBoard().getPoint(5).getColor(), Colour.BLUE);
        assertEquals(game.getBoard().getPoint(6).getCount(), 2);
        assertEquals(game.getBoard().getPoint(6).getColor(), Colour.RED);
    }

    @Test
    public void testProcessTurnPipCount() {
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[23] = 2;
        blue[22] = 2;
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        ByteArrayOutputStream outputStream = setupOutput();
        game.processTurn(0, "pip", null);
        String output = outputStream.toString();
        assertTrue(output.contains("50"));
        assertTrue(output.contains("6"));
    }

    @Test
    public void testProcessTurnHintCommand() {
        ByteArrayOutputStream outputStream = setupOutput();
        game.processTurn(0, "hint", null);

        String output = outputStream.toString();
        assertTrue(output.contains("Hint of all allowed commands:"));
        assertTrue(output.contains("Enter 'pip' to display pip count for both players"));
        assertTrue(output.contains("Enter 'roll' to roll dice"));
    }

    @Test
    public void testProcessTurnBoardCommand() {
        ByteArrayOutputStream outputStream = setupOutput();
        game.processTurn(0, "board", null);
        String output = outputStream.toString();
        assertTrue(output.contains("\u2554") && output.contains("\u255D"));
        assertTrue(output.contains("\033[0;31mO") || output.contains("\033[0;34mO"));
    }

    @Test
    public void testProcessTurnDoubleCommandAccepted() {
        setupInput("accept");
        game.processTurn(0, "double", null);
        assertEquals(1, game.getMatch().getDoubleOwner());
        assertEquals(2, game.getMatch().getDoubleCount());
    }

    @Test
    public void testProcessTurnDoubleCommandRejected() {
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        setupInput("reject\nroll 3\nroll 4\na\n");
        game.processTurn(0, "double", null);
        assertEquals(0, board.getWinner());
    }

    @Test
    public void testProcessTurnFileMove() {
        try (BufferedReader reader = new BufferedReader(new FileReader("testMove.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 2 3", reader);
            assertFalse(turnInProgress);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testProcessTurnFileHint() {
        ByteArrayOutputStream outputStream = setupOutput();
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testHint.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 2 3", reader);
            String output = outputStream.toString();
            assertFalse(turnInProgress);
            assertTrue(output.contains("Hint of all allowed commands:"));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testProcessTurnFilePip() {
        ByteArrayOutputStream outputStream = setupOutput();
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[23] = 2;
        blue[22] = 2;
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testPip.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 4 6", reader);
            String output = outputStream.toString();
            assertTrue(output.contains("50"));
            assertTrue(output.contains("6"));
            assertFalse(turnInProgress);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testProcessTurnFileBoard() {
        ByteArrayOutputStream outputStream = setupOutput();
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testBoard.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 4 6", reader);
            String output = outputStream.toString();
            assertTrue(output.contains("\u2554") && output.contains("\u255D"));
            assertTrue(output.contains("\033[0;31mO") || output.contains("\033[0;34mO"));
            assertFalse(turnInProgress);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testProcessTurnFileDouble() {
        ByteArrayOutputStream outputStream = setupOutput();
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testDoubleAfterRoll.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 4 6", reader);
            String output = outputStream.toString();
            assertTrue(output.contains("You cannot double the stakes at this time. Please enter a valid command"));
            assertFalse(turnInProgress);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testProcessTurnFileInvalidMove() {
        ByteArrayOutputStream outputStream = setupOutput();
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testInvalidMove.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 4 6", reader);
            String output = outputStream.toString();
            assertTrue(output.contains("Error: Please enter a valid command"));
            assertFalse(turnInProgress);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testHandleDoubleStakesAccepted() {
        String simulatedInput = "accept\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);
        game.handleDoubleStakes(0, null);
        assertEquals(1, game.getMatch().getDoubleOwner());
        assertEquals(2, game.getMatch().getDoubleCount());
    }

    @Test
    public void testHandleDoubleStakesRejected() {
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);
        game.setTestBoard(board);
        String simulatedInput = "reject\nroll 3\nroll 4\na\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);
        game.handleDoubleStakes(0, null);
        assertEquals(0, board.getWinner());
    }

    @Test
    public void testNewGameInitialSetup() {
        String simulatedInput = "roll 3\nroll 4\na\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);
        game.newGame(false, null);
        assertNotNull(game.getBoard());
        assertNotNull(game.getPlayers());
    }

    @Test
    public void testSetMatchLengthValid() {
        game.setMatchLength("5");
        assertEquals(5, game.getMatch().getMatchLength());
    }

    @Test
    public void testBearingOff() {
        int[] red = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);

        String simulatedInput = "a\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        boolean turnInProgress = game.processTurn(0, "roll 1 2\n", null);
        assertFalse(turnInProgress);
        assertEquals(1, board.getEnd(0).getCount());
    }

    @Test
    public void testFileStart() {
        Backgammon testGame = new Backgammon();
        ByteArrayOutputStream outputStream = setupOutput();

        testGame.fileStart("testFiles/testFileStart.txt");
        String output = outputStream.toString();

        assertEquals("utilities.Match length set to 5", 5, testGame.getMatch().getMatchLength());
        assertEquals("Player 1 is Alice", "\033[0;31mAlice\033[0m", testGame.getPlayers().getPlayerName(0));
        assertEquals("Player 2 is Bob", "\033[0;34mBob\033[0m", testGame.getPlayers().getPlayerName(1));
        assertEquals("Player is player 0 after player 1 chooses move", 0, testGame.getPlayers().getCurrentPlayer());
    }

    @Test
    public void testFileTurn() {
        ByteArrayOutputStream outputStream = setupOutput();

        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testFileTurn.txt"))) {
            game.fileTurn(reader);
            String output = outputStream.toString();
            assertTrue("Hints have been displayed", output.contains("Hint of all allowed commands:"));
            assertTrue("Moves have been printed", output.contains("please choose a move from the list above"));
            assertTrue("Player change after move chosen", output.contains("PLAYER CHANGE"));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testNewMatch() {
        setupInput("h\ny\n");
        assertTrue(game.newMatch());

        setupInput("n");
        assertFalse(game.newMatch());
    }

    @Test
    public void testOneRoll() {
        int rolls = game.oneRoll();
        assertTrue("Valid roll", rolls > 0 && rolls <= 6);
    }

    @Test
    public void testHandleDoubleFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("testFiles/testHandleDoubleFile.txt"))) {
            game.handleDoubleStakes(0, reader);
            assertEquals("Check double count", 2, game.getMatch().getDoubleCount());
            assertEquals("Player 1 has ownership of dice", 1, game.getMatch().getDoubleOwner());
            game.handleDoubleStakes(0, reader);
            assertEquals("Winner is player 0 if player 1 rejects", 0, game.getBoard().getWinner());
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    @Test
    public void testQuitGame() {
        ByteArrayOutputStream outputStream = setupOutput();
        game.quitGame(true);
        String output = outputStream.toString();
        assertTrue("Check correct message was printed", output.contains("Quitting game"));
    }

    @Test
    public void testMain() {
        String simulatedInput = "\n5\nJack\nNaoise\nroll 3\nroll 4\na\nroll 4 6\na\n";
        InputStream stdin = System.in;
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            String[] args = new String[1];
            args[0] = "test";
            Backgammon.main(args);
            String output = outputStream.toString();
            assertTrue("Check game starts", output.contains("Welcome to Backgammon!"));
            assertTrue("Check turn was completed with no errors", output.contains("Test complete!"));
        } finally {
            System.setIn(stdin);
            System.setOut(stdout);
        }
    }

    @Test
    public void testIncrementGameCount() {
        int count = game.getGamecount();
        game.incrementGameCount();
        assertEquals("Game count should have increased by 1", count + 1, game.getGamecount());
    }

    @Test
    public void testGetGameCount() {
        assertEquals("GameCount starts at 1", 1, game.getGamecount());
        game.incrementGameCount();
        assertEquals("GameCount incremented to 2", 2, game.getGamecount());
    }
}
