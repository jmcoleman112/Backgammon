import org.junit.Before;
import org.junit.Test;
import utilities.Colour;


import java.io.*;
import java.util.Scanner;




import static org.junit.Assert.*;

public class BackgammonTest {
    private Backgammon game;

    @Before
    public void setUp() {
        game = new Backgammon();

        //Set players
        String simulatedInput = "Alice\nBob\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));

        // Call the updated method to set players, passing the custom InputHandler
        game.setPlayers("", "", testInputHandler);
    }

    @Test
    public void testSetPlayers() {
        // Simulate user input for player names
        String simulatedInput = "Alice\nBob\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));

        // Call the updated method to set players, passing the custom InputHandler
        game.setPlayers("", "", testInputHandler);

        // Verify the player names
        assertEquals("\033[0;31mAlice\033[0m", game.getPlayers().getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", game.getPlayers().getPlayerName(1));
    }

    @Test
    public void testSetPlayerStrings(){
        String name1 = "Alice";
        String name2 = "Bob";
        game.setPlayers(name1, name2, new InputHandler());

        // Verify the player names
        assertEquals("\033[0;31mAlice\033[0m", game.getPlayers().getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", game.getPlayers().getPlayerName(1));
    }

    @Test
    public void testGetInputHandler() {
        // Arrange: Create an instance of the Backgammon class
        Backgammon game = new Backgammon();

        // Act: Call the getInputHandler method
        InputHandler inputHandler = game.getInputHandler();

        // Assert: Verify that the returned InputHandler is not null and is of the correct type
        assertNotNull("InputHandler should not be null", inputHandler);
    }

    @Test
    public void testWelcomeFile(){ //Test starting with file
        String simulatedInput = "test filename\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        assertEquals(game.Welcome(), "filename");
    }

    @Test
    public void testWelcomeNoFile(){
        String simulatedInput = "\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

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
        String simulatedInput = "roll 3\nroll 4\na\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        int firstPlayer = game.decideFirstPlayer();
        assertEquals(0, firstPlayer);
    }

    @Test
    public void testProcessTurnValidMove() {
        // Set up a board with a valid move
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        String simulatedInput = "a\n"; //Select move
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        boolean turnInProgress = game.processTurn(0, "roll 4 6", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoubles() {
        // Set up a board with a valid move
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        String simulatedInput = "a\na\n"; //Select move
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        boolean turnInProgress = game.processTurn(0, "roll 6 6", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoublesNoSecondMove() {
        // Set up a board with a valid move
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[18] = 2;
        Board board = new Board(red, blue);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        String simulatedInput = "a\na\n"; //Select move
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        boolean turnInProgress = game.processTurn(0, "roll 3 3", null);
        assertFalse(turnInProgress);
    }

    @Test
    public void testProcessTurnDoublesWinner() {
        // Set up a board with a valid move
        int[] red = new int[26];
        red[5] = 2;
        int[] blue = new int[26];
        blue[18] = 2;
        Board board = new Board(red, blue);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        String simulatedInput = "a\na\n"; //Select move
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

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
    public void testProcessTurnNoMoves(){
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
    public void testProcessTurnSetBoard(){
        game.processTurn(0, "setboard 0N 0N 0N 0N 0N 1B 2R 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 0N 2B 2B 0N", null);

        assertEquals(game.getBoard().getPoint(5).getCount(), 1);
        assertEquals(game.getBoard().getPoint(5).getColor(), Colour.BLUE);
        assertEquals(game.getBoard().getPoint(6).getCount(), 2);
        assertEquals(game.getBoard().getPoint(6).getColor(), Colour.RED);
    }

    @Test
    public void testProcessTurnPipCount(){
        int[] red = new int[26];
        red[24] = 2;
        int[] blue = new int[26];
        blue[23] = 2;
        blue[22] = 2;
        Board board = new Board(red, blue);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        game.processTurn(0, "pip", null);

        String output = outputStream.toString();
        assertTrue("Output should display red pip count", output.contains("50")); //Red pipcount
        assertTrue("Output should display blue pip count", output.contains("6")); //Blue pipcount
    }

    @Test
    public void testProcessTurnHintCommand(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        game.processTurn(0, "hint", null);

        String output = outputStream.toString();
        assertTrue("Output should display hints", output.contains("Hint of all allowed command:"));
        assertTrue("Output should display pip hint because game has started", output.contains("Enter 'pip' to display pip count for both players")); //Blue pipcount
        assertTrue("Output should display roll hint because haven't rolled yet", output.contains("Enter 'roll' to roll dice"));
    }

    @Test
    public void testProcessTurnBoardCommand(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        game.processTurn(0, "board", null);

        String output = outputStream.toString();
        assertTrue("Output should contain board corners", output.contains("\u2554") && output.contains("\u255D"));
        assertTrue("Output should display points for red and blue pieces", output.contains("\033[0;31mO") || output.contains("\033[0;34mO"));
    }

    @Test
    public void testProcessTurnDoubleCommandAccepted(){
        String simulatedInput = "accept\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        game.processTurn(0, "double", null);

        assertEquals(1, game.getMatch().getDoubleOwner());
        assertEquals(2, game.getMatch().getDoubleCount());
    }

    @Test
    public void testProcessTurnDoubleCommandRejected(){
        int[] red = {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] blue = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Board board = new Board(red, blue);

        game.setTestBoard(board);

        String simulatedInput = "reject\nroll 3\nroll 4\na\n"; //Reject double and start new game
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        game.processTurn(0, "double", null);

        assertEquals(0, board.getWinner());
    }

    @Test
    public void testProcessTurnFile(){
        try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {
            boolean turnInProgress = game.processTurn(0, "roll 2 3", reader);
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

        String simulatedInput = "reject\nroll 3\nroll 4\na\n"; //Reject double and start new game
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

        String simulatedInput = "a\n"; //Reject double and start new game
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));
        game.setInputHandler(testInputHandler);

        game.setTestBoard(board);
        game.getPlayers().setCurrentPlayer(0);

        boolean turnInProgress = game.processTurn(0, "roll 1 2\n", null);
        assertFalse(turnInProgress);
        assertEquals(1, board.getEnd(0).getCount());
    }
}
