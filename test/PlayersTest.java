import org.junit.Before;
import org.junit.Test;
import utilities.Colour;
import utilities.InputHandler;
import utilities.Players;

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
        assertEquals(Colour.RED.shader() + "Alice" + Colour.NONE.shader(), players.getPlayerName(0));
        assertEquals(Colour.BLUE.shader() + "Bob" + Colour.NONE.shader(), players.getPlayerName(1));
    }

    @Test
    public void testConstructor() {
        String simulatedInput = "Alice\nBob\n";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(simulatedInput.getBytes())));

        Players players = new Players(testInputHandler);

        assertEquals("\033[0;31mAlice\033[0m", players.getPlayerName(0));
        assertEquals("\033[0;34mBob\033[0m", players.getPlayerName(1));
    }

    @Test
    public void testSwitchPlayer() {
        assertEquals(0, players.getCurrentPlayer());

        players.switchPlayer();
        assertEquals(1, players.getCurrentPlayer());

        players.switchPlayer();
        assertEquals(0, players.getCurrentPlayer());
    }

    @Test
    public void testSetCurrentPlayer() {
        players.setCurrentPlayer(1);
        assertEquals(1, players.getCurrentPlayer());

        players.setCurrentPlayer(0);
        assertEquals(0, players.getCurrentPlayer());
    }

    @Test
    public void testGetPlayerName() {
        String player1Name = players.getPlayerName(0);
        String player2Name = players.getPlayerName(1);

        assertEquals(Colour.RED.shader() + "Alice" + Colour.NONE.shader(), player1Name);
        assertEquals(Colour.BLUE.shader() + "Bob" + Colour.NONE.shader(), player2Name);
    }
}
