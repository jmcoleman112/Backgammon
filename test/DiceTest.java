import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceTest {

    private Dice dice;

    @Before
    public void setUp() {
        dice = new Dice();
    }

    @Test
    public void testInitialDiceValues() {
        // Ensure the initial dice values are -1
        int[] values = dice.getDiceValues();
        assertEquals("Expected dice1 to be -1", -1, values[0]);
        assertEquals("Expected dice2 to be -1", -1, values[1]);
    }

    @Test
    public void testFirstRoll() {
        dice.firstRoll();
        int[] values = dice.getDiceValues();

        // Check that dice1 has a valid roll (1-6) after the first roll
        assertTrue("Expected dice1 to be between 1 and 6", values[0] >= 1 && values[0] <= 6);

        // Ensure dice2 remains unchanged at -1
        assertEquals("Expected dice2 to remain -1 after firstRoll", -1, values[1]);
    }

    @Test
    public void testRollDice() {
        int[] values = dice.rollDice();

        // Verify dice1 and dice2 have valid rolls (1-6)
        if (values.length == 2) {
            assertTrue("Expected dice1 to be between 1 and 6", values[0] >= 1 && values[0] <= 6);
            assertTrue("Expected dice2 to be between 1 and 6", values[1] >= 1 && values[1] <= 6);
        } else if (values.length == 4) {
            // If doubles, check that all values are the same and valid (1-6)
            assertEquals("Expected all four dice to be equal", values[0], values[1]);
            assertEquals("Expected all four dice to be equal", values[1], values[2]);
            assertEquals("Expected all four dice to be equal", values[2], values[3]);
            assertTrue("Expected dice value to be between 1 and 6", values[0] >= 1 && values[0] <= 6);
        } else {
            fail("Unexpected dice values array size: " + values.length);
        }
    }

    @Test
    public void testDoublesInRollDice() {
        // Simulate rolling doubles by overriding Math.random
        boolean doublesOccurred = false;
        for (int i = 0; i < 100; i++) {
            int[] values = dice.rollDice();
            if (values.length == 4) {
                doublesOccurred = true;
                break;
            }
        }
        assertTrue("Expected doubles to occur at least once in 100 rolls", doublesOccurred);
    }
}
