import utilities.Colour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoveHandler {//Class to check and execute moves
    private final Board board;
    List<Character> availablemoves = new ArrayList<>();
    private List<int[]> validMoves = new ArrayList<>();
    private Map<Character, int[]> moveMap = new HashMap<>();


    public MoveHandler(Board board) {
        this.board = board;
    }



    public void legalmoves(int player, int dice1, int dice2) {
        List<Integer> locs;
        if (player == 0) {
            locs = board.getColoredPoints(Colour.BLUE);
        } else {
            locs = board.getColoredPoints(Colour.RED);
        }

        List<int[]> validMoves = new ArrayList<>();
        searchMoves(locs, dice1, dice2, validMoves, player);

        System.out.println("Valid Moves:");
        char moveLabel = 'a';
        for (int[] move : validMoves) {
            if (player == 0) {
                System.out.printf("%c) Move %d -> %d, and then %d -> %d%n", moveLabel, move[0] + 1, move[1] + 1, move[2] + 1, move[3] + 1);
            } else {
                System.out.printf("%c) Move %d -> %d, and then %d -> %d%n", moveLabel, 24 - move[0], 24 - move[1], 24 - move[2], 24 - move[3]);
            }
            availablemoves.add(moveLabel);
            moveMap.put(moveLabel, move.clone());
            moveLabel++;
        }
    }


    public Boolean isValidMoveCommand(String moveInput) {
        char label = moveInput.charAt(0);
        return availablemoves.contains(label);
}


    public void searchMoves(List<Integer> locs, int dice1, int dice2, List<int[]> validMoves, int player) {
        // Adjusted DFS call with an array to hold 4 positions: [x1, y1, x2, y2]
        dfs(locs, dice1, dice2, validMoves, player, new int[4], 0, 0);
    }

    private void dfs(List<Integer> locs, int dice1, int dice2, List<int[]> validMoves, int player, int[] move, int depth, int usedDice) {
        if (depth == 2) { // When both moves are used
            validMoves.add(move.clone());
            return;
        }

        int direction = (player == 0) ? 1 : -1; // Direction multiplier based on player

        for (int loc : locs) {
            int target = loc + direction * (usedDice == 0 ? dice1 : dice2);
            if (isLegalMove(player, loc, target)) { // Check if the move is legal
                move[depth * 2] = loc;       // Start position for this move
                move[depth * 2 + 1] = target; // End position for this move

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                newLocs.remove(Integer.valueOf(loc)); // Remove the original position
                newLocs.add(target); // Add the new position

                dfs(newLocs, dice1, dice2, validMoves, player, move, depth + 1, usedDice + 1);
            }
        }
    }

    // Method to check if a move is legal based on backgammon rules
    private boolean isLegalMove(int player, int start, int target) {
        if((board.getPointColor(target) ==board.getPointColor(start).returnopp()) && board.getPointCount(target) > 1){
            return false;
        }
        if(start > 23 || start < 0){
            return false;
        }

        return true;
    }



    public int[] getMoveFromCommand(String command) {
        if (command.length() != 1) {
            throw new IllegalArgumentException("Command must be a single character.");
        }
        char label = command.charAt(0);
        return moveMap.getOrDefault(label, null); // Return the associated move or null if not found
    }


    public boolean checkMove(int from, int to) {
        Point source = board.getPoint(from);
        Colour sColour = source.getColor();

        Point destination = board.getPoint(to);
        Colour dColour = destination.getColor();

        if (source.getCount() == 0) return false; //Empty point

        else return destination.getCount() <= 1 || sColour == dColour; //Legal move
    }

    public void executeMove(int from, int to){ //Use array index not pip number

        Point source = board.getPoint(from);
        Colour sColour = source.getColor();

        Point destination = board.getPoint(to);
        Colour dColour = destination.getColor();

        if(source.getCount() == 0){
            System.out.println("Error point is empty\n\n");
            return;
        }

        source.setCount(source.getCount() - 1); //Decrement checker count
        if(source.getCount() == 0){ //No more checkers on point
            source.setColor(Colour.NONE);
        }

        if (sColour == dColour){ //Same colour
            destination.setCount(destination.getCount() + 1); //Increment checker count
        }
        else {
            destination.setColor(sColour);
            destination.setCount(1); //Set to one
            if(dColour != Colour.NONE){ //Pipped
                int bColour = (dColour == Colour.BLUE) ? 1 : 0;
                Bar bar = board.getBar(bColour);
                bar.setCount(bar.getCount() + 1);
            }
        }
    }

    public void moveToEnd(int index){
        Point point = board.getPoint(index);

        if(point.getCount() == 0){
            System.out.println("Error point is empty\n\n");
            return;
        }

        End end; //Add checker to end
        if(point.getColor() == Colour.RED){
            end = board.getEnd(0);
        }
        else {
            end = board.getEnd(1);
        }
        end.setCount(end.getCount() + 1);


        point.setCount(point.getCount() - 1); //Decrement checker count
        if(point.getCount() == 0){ //No more checkers on point
            point.setColor(Colour.NONE);
        }

    }
}
