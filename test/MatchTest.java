import org.junit.Test;
import org.junit.Before;
import utilities.Board;
import utilities.Colour;
import utilities.Match;

import static org.junit.Assert.*;

public class MatchTest {
    private Match match;

    @Before
    public void setUp() {
        match = new Match();
    }

    @Test
    public void testSetScorePlayer0() {
        match.setScore(3, 0);
        assertEquals(3, match.getRedScore());
    }

    @Test
    public void testSetScorePlayer1() {
        match.setScore(5, 1);
        assertEquals(5, match.getBlueScore());
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
        int[] redPointsSingle = new int[26];
        int[] bluePointSingle = new int[26];
        redPointsSingle[0] = 1;
        redPointsSingle[25] = 14;
        bluePointSingle[25] = 15;
        Board singleBoard = new Board(redPointsSingle, bluePointSingle);
        match.updateScore(1, singleBoard);
        assertEquals(1, match.getBlueScore());
        match.updateDoubleCount();
        match.updateScore(1, singleBoard);
        assertEquals(3, match.getBlueScore());

        int[] redPointsGammon = new int[26];
        int[] bluePointsGammon = new int[26];
        bluePointSingle[25] = 15;
        redPointsGammon[0] = 14;
        redPointsGammon[10] = 1;
        Board gammonBoard = new Board(redPointsGammon, bluePointsGammon);
        match.updateScore(1, gammonBoard);
        assertEquals(5, match.getBlueScore());
        match.updateDoubleCount();
        match.updateScore(1, gammonBoard);
        assertEquals(9, match.getBlueScore());

        int[] redPointsBackgammon = new int[26];
        int[] bluePointsBackgammon = new int[26];
        bluePointsBackgammon[23] = 14;
        bluePointsBackgammon[24] = 1;
        Board backgammonBoard = new Board(redPointsBackgammon, bluePointsBackgammon);
        match.updateScore(0, backgammonBoard);
        assertEquals(3, match.getRedScore());
        match.updateDoubleCount();
        match.updateScore(0, backgammonBoard);
        assertEquals(9, match.getRedScore());
    }

    @Test
    public void printScore() {
        int[] redPoints = new int[26];
        int[] bluePoints = new int[26];
        redPoints[0] = 1;
        redPoints[25] = 14;
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
        int[] bluePoints = new int[26];
        bluePoints[0] = 1;
        bluePoints[25] = 14;
        redPoints[25] = 15;
        Board board = new Board(redPoints, bluePoints);

        match.updateScore(0, board);
        assertEquals(1, match.getRedScore());
    }

    @Test
    public void getBlueScore() {
        int[] redPoints = new int[26];
        int[] bluePoints = new int[26];
        redPoints[0] = 1;
        redPoints[25] = 14;
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
        match.setMatchLength(2);
        match.updateScore(0, new Board());
        assertTrue(match.noMatchWinner());
        match.updateScore(0, new Board());
        assertFalse(match.noMatchWinner());
    }

    @Test
    public void returnPointsScored() {
        int[] redPointsSingle = new int[26];
        int[] bluePointSingle = new int[26];
        redPointsSingle[0] = 1;
        redPointsSingle[25] = 14;
        bluePointSingle[25] = 15;
        Board singleBoard = new Board(redPointsSingle, bluePointSingle);
        assertEquals(1, match.returnPointsScored(1, singleBoard));
        match.updateDoubleCount();
        assertEquals(2, match.returnPointsScored(1, singleBoard));
        match.updateScore(1, singleBoard);

        int[] redPointsGammon = new int[26];
        int[] bluePointsGammon = new int[26];
        bluePointSingle[25] = 15;
        redPointsGammon[0] = 14;
        redPointsGammon[10] = 1;
        Board gammonBoard = new Board(redPointsGammon, bluePointsGammon);
        assertEquals(2, match.returnPointsScored(1, gammonBoard));
        match.updateDoubleCount();
        assertEquals(4, match.returnPointsScored(1, gammonBoard));
        match.updateScore(1, gammonBoard);

        int[] redPointsBackgammon = new int[26];
        int[] bluePointsBackgammon = new int[26];
        redPointsBackgammon[0] = 0;
        bluePointsBackgammon[23] = 14;
        bluePointsBackgammon[24] = 1;
        Board backgammonBoard = new Board(redPointsBackgammon, bluePointsBackgammon);
        assertEquals(3, match.returnPointsScored(0, backgammonBoard));
        match.updateDoubleCount();
        assertEquals(6, match.returnPointsScored(0, backgammonBoard));
    }

    @Test
    public void getMatchWinner() {
        match.setMatchLength(2);
        match.updateScore(0, new Board());
        match.updateScore(0, new Board());
        match.noMatchWinner();
        assertEquals(0, match.getMatchWinner());
    }
}
