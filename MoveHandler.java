import utilities.Colour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoveHandler {//Class to check and execute moves
    private final Board board;
    List<Character> availablemoves = new ArrayList<>();
    private Map<Character, int[]> moveMap = new HashMap<>();



    public MoveHandler(Board board) {
        this.board = board;
    }


    public boolean legalmoves(int player, int dice1, int dice2) {
        List<Integer> locs;
        if (player == 1) {
            locs = board.getColoredPoints(Colour.BLUE);
        } else {
            locs = board.getColoredPoints(Colour.RED);
        }

        List<int[]> validMoves = new ArrayList<>();
        int[] move = new int[4];

        // Start DFS to explore moves, including re-entry if necessary
        if (board.getBar(player).getCount() > 0) {
            System.out.print("Player " + (player + 1) + " has checkers on the bar. ");
            dfsWithReentry(locs, dice1, dice2, validMoves, player, move, 0, 0, board.getBar(player).getCount());
        } else {
            dfs(locs, dice1, dice2, validMoves, player, move, 0, 0);
        }

        if(validMoves.isEmpty()) {
            System.out.println("No moves available");
            return false;
        }
        // Display valid moves
        System.out.println("Valid Moves:");
        char moveLabel = 'a';
        boolean twopart = false;
        int maxdist = 0;
        for(int[] m: validMoves){
            if(m[0] != m[1] && m[2] != m[3]) {
                twopart=true;
            }
            maxdist = Math.max(maxdist, Math.max(Math.abs(m[0]-m[1]), Math.abs(m[2]-m[3])));
        }
        for (int[] m : validMoves) {
            if (player == 0) {
                // Check if the first move is a re-entry or a regular/bearing-off move
                String firstMove = (m[0] == 24)
                        ? String.format("Re-enter from off-board to %d", m[1] + 1)
                        : (m[1] < 0 || m[1] > 23) // Allow bearing off on the first move
                        ? String.format("bear off from Pt. %d", m[0] + 1)
                        : String.format("Move %d -> %d", m[0] + 1, m[1] + 1);

                // Check if the second move is a regular move or bearing-off
                String secondMove = (m[2] == 24)
                        ? String.format("Re-enter from off-board to %d", m[3] + 1)
                        : (m[3] < 0 || m[3] > 23) // Allow bearing off on the first move
                        ? String.format("bear off from Pt. %d", m[2] + 1)
                        : String.format("Move %d -> %d", m[2] + 1, m[3] + 1);
                if(!twopart && m[2]==m[3]){
                    System.out.printf("%c) %s%n", moveLabel, firstMove);
                }
                else if(!twopart && m[0] == m[1]){
                    System.out.printf("%c) %s%n", moveLabel, secondMove);
                }
                else if(m[0] != m[1]&& m[2] != m[3]){
                    System.out.printf("%c) %s, and then %s%n", moveLabel, firstMove, secondMove);
                }
            } else {
                // Check if the first move is a re-entry or a regular/bearing-off move
                String firstMove = (m[0] == -1)
                        ? String.format("Re-enter from off-board to %d", 24 - m[1])
                        : (m[1] < 0 || m[1] > 23) // Allow bearing off on the first move
                        ? String.format("bear off from Pt. %d", 24 - m[0])
                        : String.format("Move %d -> %d", 24 - m[0], 24 - m[1]);

                // Check if the second move is a regular move or bearing-off
                String secondMove = (m[2] == -1)
                        ? String.format("Re-enter from off-board to %d", 24 - m[3])
                        : (m[3] < 0 || m[3] > 23) // Allow bearing off on the first move
                        ? String.format("bear off from Pt. %d", 24 - m[2])
                        : String.format("Move %d -> %d", 24 - m[2], 24 - m[3]);
                if(!twopart && m[2]==m[3]){
                    System.out.printf("%c) %s%n", moveLabel, firstMove);
                }
                else if(!twopart && m[0] == m[1]){
                    System.out.printf("%c) %s%n", moveLabel, secondMove);
                }
                else if(m[0] != m[1]&& m[2] != m[3]){
                    System.out.printf("%c) %s, and then %s%n", moveLabel, firstMove, secondMove);
                }
            }
            availablemoves.add(moveLabel);
            moveMap.put(moveLabel, m.clone());
            moveLabel++;
        }
        return true;
    }

    public Boolean isValidMoveCommand(String moveInput) {
        if(moveInput.length() == 1) {
            char label = moveInput.charAt(0);
            return availablemoves.contains(label);
        }
        else return false;
}

    private void dfsWithReentry(List<Integer> locs, int dice1, int dice2, List<int[]> validMoves, int player, int[] move, int depth, int usedDice, int barcount) {
        int direction = (player == 1) ? 1 : -1;

        if (depth == 2) {
            validMoves.add(move.clone());
            return;
        }
        barcount--;
        // Re-entry moves from -1 (off-board) for player 0 and player 1
        int reentryTarget1 = (player == 1 ? -1 : 24) + direction * dice1;
        int reentryTarget2 = (player == 1 ? -1 : 24) + direction * dice2;

        // Use dice1 for re-entry and explore the second move with dice2
        if (isLegalMove(player, -1, reentryTarget1)) {
            move[0] = player==1 ? -1 : 24; // Set re-entry point as -1 for off-board
            move[1] = reentryTarget1;
            if(barcount ==0 ){
                dfs(locs, 0, dice2, validMoves, player, move, depth+1, usedDice+1);
            }
            else{
                dfsWithReentry(locs, 0, dice2, validMoves, player, move, depth+1, usedDice+1, barcount);
            }
        }

        // Use dice2 for re-entry and explore the second move with dice1
        if (isLegalMove(player, -1, reentryTarget2)) {
            move[0] = player==1 ? -1 : 24; // Set re-entry point as -1 for off-board
            move[1] = reentryTarget2;
            if(barcount ==0){
                dfs(locs, dice1, 0, validMoves, player, move, 1, 0);
            }
            else{
                dfsWithReentry(locs, dice1, 0, validMoves, player, move, 1, 0, barcount);
            }
        }
    }

    private void dfs(List<Integer> locs, int dice1, int dice2, List<int[]> validMoves, int player, int[] move, int depth, int usedDice) {
        // If both moves are used, add the move to valid moves and return
        if (depth == 2) {
            validMoves.add(move.clone());
            return;
        }

        int direction = (player == 1) ? 1 : -1; // Direction multiplier based on player

        // Explore normal moves using remaining dice for all board positions in `locs`
        for (int loc : locs) {
            int target = loc + direction * (usedDice == 0 ? dice1 : dice2);
            if (isLegalMove(player, loc, target)) {
                move[depth * 2] = loc;
                move[depth * 2 + 1] = target;

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                if(board.getPoint(loc).getCount() == 1){
                    newLocs.remove(Integer.valueOf(loc)); // Remove the original position if only one counter on point
                }
                newLocs.add(target); // Add the new position

                // Recursive DFS with incremented depth
                dfs(newLocs, dice1, dice2, validMoves, player, move, depth + 1, usedDice + 1);
            }
        }
    }

        private boolean isLegalMove(int player, int start, int target) {

        if((target < 0 || target > 23) && !board.bearoffcheck(player)) { // Check if target is off-board
            return false;
        }

        if (start == -1 || start == 25) { // Check if it’s a re-entry move for players 1 and 0
            Colour targetColour = board.getPointColor(target);
            return targetColour == Colour.NONE || targetColour == board.getPlayerColor(player)
                    || board.getPointCount(target) == 1; // Allow re-entry if empty, same color, or only one opponent checker
        } else {
            // Regular legality check for non-re-entry moves
            if ((board.getPointColor(target) == board.getPointColor(start).returnopp()) && board.getPointCount(target) > 1) {
                return false;
            }
            return true;
        }
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

    public void executeMove(int from, int to) { // Use array index, -1 for off-board
        Colour sColour;

        // Check if re-entry is needed from the bar (from -1)
        if (from == -1 || from == 24) {
            int barIndex = (to < 12) ? 1 : 0; // Assume player 1 re-enters on the lower half (barIndex 1), player 0 on upper (barIndex 0)
            Bar bar = board.getBar(barIndex);

            if (bar.getCount() == 0) {
                System.out.println("Error: no checkers on the bar to re-enter.");
                return;
            }

            // Set source color for re-entry
            sColour = (barIndex == 1) ? Colour.BLUE : Colour.RED;
            bar.setCount(bar.getCount() - 1); // Remove one checker from the bar
        }

        else {
            // Normal move on the board
            Point source = board.getPoint(from);
            sColour = source.getColor();

            if (source.getCount() == 0) {
                System.out.println("Error: source point is empty");
                return;
            }

            // Decrement checker count on source point
            source.setCount(source.getCount() - 1);
            if (source.getCount() == 0) { // No more checkers on point
                source.setColor(Colour.NONE);
            }
        }

        // Proceed with moving to the destination
        if (to >= 0 && to <= 23) {
            Point destination = board.getPoint(to);
            Colour dColour = destination.getColor();

            // If destination has the same color, just increment count
            if (sColour == dColour) {
                destination.setCount(destination.getCount() + 1);
            } else {
                // If destination has an opponent checker or is empty, place the checker and handle pipping
                destination.setColor(sColour);
                destination.setCount(1);

                // If opponent checker was present, move it to the bar
                if (dColour != Colour.NONE) { // Checker was "pipped"
                    int opponentBarIndex = (dColour == Colour.BLUE) ? 1 : 0;
                    Bar opponentBar = board.getBar(opponentBarIndex);
                    opponentBar.setCount(opponentBar.getCount() + 1);
                }
            }
        } else {
            // If `to` is outside 0-23, it indicates bearing off
            moveToEnd(from);
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
