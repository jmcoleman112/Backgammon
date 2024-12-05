import org.junit.Before;
import org.junit.Test;
import utilities.Colour;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class PlayersTest {

    private Players players;

    @Before
    public void setUp() {
        players = new Players("Alice", "Bob");
    }


    @Test
    public void testInitialization() {
        // Test player names after initialization
        assertEquals("Player 1 name should be Alice (with RED color)",
                Colour.RED.shader() + "Alice" + Colour.NONE.shader(),
                players.getPlayerName(0));
        assertEquals("Player 2 name should be Bob (with BLUE color)",
                Colour.BLUE.shader() + "Bob" + Colour.NONE.shader(),
                players.getPlayerName(1));
    }

    @Test
    public void testConstructor(){
        String simulatedInput = "Alice\nBob\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));

        // Call the updated method to set players, passing the custom InputHandler
        Players players = new Players(testInputHandler);

        // Verify the player names
        assertEquals("\033[0;31mAlice\033[0m", players.getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", players.getPlayerName(1));

    }

    @Test
    public void testSwitchPlayer() {
        // Test initial current player
        assertEquals("Initial current player should be 0", 0, players.getCurrentPlayer());

        // Switch player and test
        players.switchPlayer();
        assertEquals("Current player should now be 1", 1, players.getCurrentPlayer());

        // Switch player again and test
        players.switchPlayer();
        assertEquals("Current player should now be back to 0", 0, players.getCurrentPlayer());
    }

    @Test
    public void testSetCurrentPlayer() {
        // Set current player to 1 and test
        players.setCurrentPlayer(1);
        assertEquals("Current player should be set to 1", 1, players.getCurrentPlayer());

        // Set current player to 0 and test
        players.setCurrentPlayer(0);
        assertEquals("Current player should be set to 0", 0, players.getCurrentPlayer());
    }

    @Test
    public void testGetPlayerName() {
        // Test getting player names
        String player1Name = players.getPlayerName(0);
        String player2Name = players.getPlayerName(1);

        assertEquals("Player 1 name should be Alice (with RED color)",
                Colour.RED.shader() + "Alice" + Colour.NONE.shader(),
                player1Name);
        assertEquals("Player 2 name should be Bob (with BLUE color)",
                Colour.BLUE.shader() + "Bob" + Colour.NONE.shader(),
                player2Name);
    }
}
