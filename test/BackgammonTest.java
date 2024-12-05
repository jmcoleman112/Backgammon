import org.junit.Before;
import org.junit.Test;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
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
