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
        int[] values = dice.getDiceValues();
        assertEquals(-1, values[0]);
        assertEquals(-1, values[1]);
    }

    @Test
    public void testFirstRoll() {
        dice.firstRoll();
        int[] values = dice.getDiceValues();
        assertTrue(values[0] >= 1 && values[0] <= 6);
        assertEquals(-1, values[1]);
    }

    @Test
    public void testRollDice() {
        int[] values = dice.rollDice();
        if (values.length == 2) {
            assertTrue(values[0] >= 1 && values[0] <= 6);
            assertTrue(values[1] >= 1 && values[1] <= 6);
        } else if (values.length == 4) {
            assertEquals(values[0], values[1]);
            assertEquals(values[1], values[2]);
            assertEquals(values[2], values[3]);
            assertTrue(values[0] >= 1 && values[0] <= 6);
        } else {
            fail("Unexpected dice values array size: " + values.length);
        }
    }

    @Test
    public void testDoublesInRollDice() {
        boolean doublesOccurred = false;
        for (int i = 0; i < 100; i++) {
            int[] values = dice.rollDice();
            if (values.length == 4) {
                doublesOccurred = true;
                break;
            }
        }
        assertTrue(doublesOccurred);
    }
}
