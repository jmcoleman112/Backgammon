import org.junit.Test;
import org.junit.Before;
import utilities.Colour;

import static org.junit.Assert.*;

public class MatchTest {
    private Match match;

    @Before
    public void setUp() {
        match = new Match();
    }

    @Test
    public void testSetScorePlayer0(){
        match.setScore(3, 0);
        assertEquals("Score should be 3", 3, match.getRedScore());
    }

    @Test
    public void testSetScorePlayer1(){
        match.setScore(5, 1);
        assertEquals("Score should be 5", 5, match.getBlueScore());
    }

    @Test
    public void setMatchLength() {
        match.setMatchLength(7);
        assertEquals(7, match.getMatchLength());
    }

    @Test
    public void setDoubleOwner() {
        match.setDoubleOwner(1);
        assertEquals(1, match.getDoubleOwner());
    }

    @Test
    public void getDoubleOwner() {
        match.setDoubleOwner(0);
        assertEquals(0, match.getDoubleOwner());
    }

    @Test
    public void updateScore() {
        // Single game win setup: The loser has borne off at least one checker
        int[] redPointsSingle = new int[26];
        int[] bluePointSingle = new int[26]; //Blue is winner
        redPointsSingle[0] = 1; // Red has a single piece remaining (loser)
        redPointsSingle[25] = 14; //End count
        bluePointSingle[25] = 15;
        Board singleBoard = new Board(redPointsSingle, bluePointSingle);
        match.updateScore(1, singleBoard); // Blue wins
        assertEquals(1, match.getBlueScore()); // Single game win awards 1 point
        match.updateDoubleCount();
        match.updateScore(1, singleBoard); // Blue wins
        assertEquals(3, match.getBlueScore()); // Single game win awards 2 points with double, Blue now has 3

        // Gammon win setup: The loser has not borne off any checkers
        int[] redPointsGammon = new int[26]; // Red has pieces on the board
        int[] bluePointsGammon = new int[26];
        bluePointSingle[25] = 15;
        redPointsGammon[0] = 15; // 15 points left on board
        Board gammonBoard = new Board(redPointsGammon, bluePointsGammon);
        match.updateScore(1, gammonBoard); // Blue wins
        assertEquals(5, match.getBlueScore()); // Gammon win awards 2 points, 3+2=5
        match.updateDoubleCount();
        match.updateScore(1, gammonBoard); // Blue wins
        assertEquals(9, match.getBlueScore()); // 4 points with double, 5+4=9

        // Backgammon win setup: The loser has not borne off and has pieces on the bar or in the winner's home board
        int[] redPointsBackgammon = new int[26];
        int[] bluePointsBackgammon = new int[26];
        redPointsBackgammon[0] = 0; // No red pieces on the board
        bluePointsBackgammon[23] = 14; // One counter on bar
        bluePointsBackgammon[24] = 1; //One on bar
        Board backgammonBoard = new Board(redPointsBackgammon, bluePointsBackgammon);
        match.updateScore(0, backgammonBoard); // Red wins
        assertEquals(3, match.getRedScore()); // Backgammon win awards 3 points
        match.updateDoubleCount();
        match.updateScore(0, gammonBoard); // Blue wins
        assertEquals(9, match.getRedScore()); // 6 points with double, 3+6=9
    }


    @Test
    public void printScore() {
        int[] redPoints = new int[26];
        int[] bluePoints = new int[26]; //Blue is winner
        redPoints[0] = 1; // Red has a single piece remaining (loser)
        redPoints[25] = 14; //End count
        bluePoints[25] = 15;
        Board board = new Board(redPoints, bluePoints);

        match.setMatchLength(5);
        assertEquals(Colour.RED.shader() + "0" + Colour.NONE.shader() + "-" + Colour.BLUE.shader() + "0" + Colour.NONE.shader(), match.printScore());
        match.updateScore(1, board);
        assertEquals(Colour.RED.shader() + "0" + Colour.NONE.shader() + "-" + Colour.BLUE.shader() + "1" + Colour.NONE.shader(), match.printScore());

    }

    @Test
    public void getRedScore() {
        int[] redPoints = new int[26];
        int[] bluePoints = new int[26]; //Blue is winner
        bluePoints[0] = 1; // Red has a single piece remaining (loser)
        bluePoints[25] = 14; //End count
        redPoints[25] = 15;
        Board board = new Board(redPoints, bluePoints);

        match.updateScore(0, board);
        assertEquals(1, match.getRedScore());
    }

    @Test
    public void getBlueScore() {
        int[] redPoints = new int[26];
        int[] bluePoints = new int[26]; //Blue is winner
        redPoints[0] = 1; // Red has a single piece remaining (loser)
        redPoints[25] = 14; //End count
        bluePoints[25] = 15;
        Board board = new Board(redPoints, bluePoints);

        match.updateScore(1, board);
        assertEquals(1, match.getBlueScore());
    }

    @Test
    public void getMatchLength() {
        match.setMatchLength(5);
        assertEquals(5, match.getMatchLength());
    }

    @Test
    public void getDoubleCount() {
        match.updateDoubleCount();
        assertEquals(2, match.getDoubleCount());
    }

    @Test
    public void updateDoubleCount() {
        match.updateDoubleCount();
        match.updateDoubleCount();
        assertEquals(4, match.getDoubleCount());
    }

    @Test
    public void doublelegality() {
        match.setDoubleOwner(1);
        assertTrue(match.doublelegality(1));
        assertFalse(match.doublelegality(0));
    }

    @Test
    public void noMatchWinner() {
        match.setMatchLength(5);
        match.updateScore(0, new Board()); // Assuming this updates the RedScore
        assertTrue(match.noMatchWinner());
        match.updateScore(0, new Board()); // Assuming enough updates to hit matchLength
        assertFalse(match.noMatchWinner());
    }

    @Test
    public void returnPointsScored() {
        // Single game win setup: The loser has borne off at least one checker
        int[] redPointsSingle = new int[26];
        int[] bluePointSingle = new int[26]; //Blue is winner
        redPointsSingle[0] = 1; // Red has a single piece remaining (loser)
        redPointsSingle[25] = 14; //End count
        bluePointSingle[25] = 15;
        Board singleBoard = new Board(redPointsSingle, bluePointSingle);
        assertEquals(1, match.returnPointsScored(1, singleBoard));
        match.updateDoubleCount();
        assertEquals(2, match.returnPointsScored(1, singleBoard));
        match.updateScore(1, singleBoard); //To reset doubleCount

        // Gammon win setup: The loser has not borne off any checkers
        int[] redPointsGammon = new int[26]; // Red has pieces on the board
        int[] bluePointsGammon = new int[26];
        bluePointSingle[25] = 15;
        redPointsGammon[0] = 15; // 15 points left on board
        Board gammonBoard = new Board(redPointsGammon, bluePointsGammon);
        assertEquals(2, match.returnPointsScored(1, gammonBoard));
        match.updateDoubleCount();
        assertEquals(4, match.returnPointsScored(1, gammonBoard));
        match.updateScore(1, gammonBoard); //To reset doubleCount

        // Backgammon win setup: The loser has not borne off and has pieces on the bar or in the winner's home board
        int[] redPointsBackgammon = new int[26];
        int[] bluePointsBackgammon = new int[26];
        redPointsBackgammon[0] = 0; // No red pieces on the board
        bluePointsBackgammon[23] = 14; // One counter on bar
        bluePointsBackgammon[24] = 1; //One on bar
        Board backgammonBoard = new Board(redPointsBackgammon, bluePointsBackgammon);
        assertEquals(3, match.returnPointsScored(0, backgammonBoard));
        match.updateDoubleCount();
        assertEquals(6, match.returnPointsScored(0, backgammonBoard));
    }

    @Test
    public void getMatchWinner() {
        match.setMatchLength(5);
        match.updateScore(0, new Board());
        match.updateScore(0, new Board());
        // Simulate enough updates to reach matchLength
        match.noMatchWinner();
        assertEquals(0, match.getMatchWinner());
    }
}
