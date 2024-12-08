
import org.junit.Before;
import org.junit.Test;
import Controller.InputHandler;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class InputHandlerTest {

    private InputHandler inputHandler;

    @Before
    public void setUp() {
        inputHandler = new InputHandler();
    }

    @Test
    public void testGetInput() {
        String testInput = "test";
        InputHandler testInputHandler = new InputHandler();
        testInputHandler.setScanner(new Scanner(new ByteArrayInputStream(testInput.getBytes())));

        String input = testInputHandler.getInput();

        assertEquals("Check simulated input", testInput, input);
    }

    @Test
    public void isHintCommand() {
        assertTrue(inputHandler.isHintCommand("hint"));
        assertTrue(inputHandler.isHintCommand("HINT"));
        assertFalse(inputHandler.isHintCommand("notHint"));
    }

    @Test
    public void isQuitCommand() {
        assertTrue(inputHandler.isQuitCommand("Q"));
        assertTrue(inputHandler.isQuitCommand("q"));
        assertFalse(inputHandler.isQuitCommand("quit"));
    }

    @Test
    public void isBoardCommand() {
        assertTrue(inputHandler.isBoardCommand("board"));
        assertTrue(inputHandler.isBoardCommand("BOARD"));
        assertFalse(inputHandler.isBoardCommand("gameboard"));
    }

    @Test
    public void isDoubleCommand() {
        assertTrue(inputHandler.isDoubleCommand("double"));
        assertTrue(inputHandler.isDoubleCommand("DOUBLE"));
        assertFalse(inputHandler.isDoubleCommand("doub"));
    }

    @Test
    public void isfileCommand() {
        assertTrue(inputHandler.isfileCommand("test file"));
        assertTrue(inputHandler.isfileCommand("TEST something"));
        assertFalse(inputHandler.isfileCommand("tes something"));
    }

    @Test
    public void isRollCommand() {
        assertTrue(inputHandler.isRollCommand("roll dice"));
        assertTrue(inputHandler.isRollCommand("ROLL test"));
        assertFalse(inputHandler.isRollCommand("role"));
    }

    @Test
    public void isrolltestcommand() {
        assertTrue(inputHandler.isrolltestcommand("roll 1 2"));
        assertTrue(inputHandler.isrolltestcommand("roll 6 6"));
        assertFalse(inputHandler.isrolltestcommand("roll 0 7"));
        assertFalse(inputHandler.isrolltestcommand("roll test"));
    }

    @Test
    public void isrollOnetestcommand() {
        assertTrue(inputHandler.isrollOnetestcommand("roll 1"));
        assertTrue(inputHandler.isrollOnetestcommand("roll 6"));
        assertFalse(inputHandler.isrollOnetestcommand("roll 7"));
        assertFalse(inputHandler.isrollOnetestcommand("roll test"));
    }

    @Test
    public void isPipCommand() {
        assertTrue(inputHandler.isPipCommand("pip"));
        assertTrue(inputHandler.isPipCommand("PIP"));
        assertFalse(inputHandler.isPipCommand("pop"));
    }

    @Test
    public void closeScanner() {
        inputHandler.closeScanner();
    }
}
