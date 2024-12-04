/**
 * The MoveHandler class is responsible for checking and executing moves in the Backgammon game.
 * It provides methods to determine legal moves, validate move commands, and execute moves on the board.
 * The class uses depth-first search (DFS) algorithms to explore possible moves, including handling re-entry from the bar.
 * It also manages collections of available moves and their corresponding commands.
 *
 * <p>Features include:</p>
 * <ul>
 *   <li>Determining legal moves based on the current state of the board and dice rolls.</li>
 *   <li>Validating move commands entered by the player.</li>
 *   <li>Executing moves, including normal moves, re-entry from the bar, and bearing off.</li>
 *   <li>Managing the board state and updating the positions of checkers.</li>
 * </ul>
 *
 *
 * <p>Author: jmcoleman112</p>
 *
 * @see Board
 * @see Dice
 * @see InputHandler
 * @see MoveHandler
 * @see Match
 * @see Players
 * @see Display
 */


import utilities.Colour;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MoveHandler {//Class to check and execute moves
    private Board board;
    List<String> availablemoves = new ArrayList<>();
    private Map<String, int[]> moveMap = new HashMap<>();


    /**
     * Constructs a MoveHandler with the specified board.
     *
     * @param board the board to be used by the MoveHandler
     */
    public MoveHandler(Board board) {
        this.board = board;
    }

    /**
     * Determines the legal moves for a player based on the dice rolls.
     *
     * @param player the player making the move
     * @param dice1  the value of the first die
     * @param dice2  the value of the second die
     * @return true if there are legal moves available, false otherwise
     */
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
            System.out.println("Player " + (player + 1) + " has checkers on the bar. ");
            dfsWithReentry(locs, dice1, dice2, validMoves, player, move, 0, 0, board.getBar(player).getCount());
        } else {
            dfs(locs, dice1, dice2, validMoves, player, move, 0);
        }

        if(validMoves.isEmpty()) {
            System.out.println("No moves available");
            return false;
        }

        int index = 0;
        boolean twopart = false;
        int maxdist = 0;
        for(int[] m: validMoves){
            if(m[0] != m[1] && m[2] != m[3]) {
                twopart=true;
            }
            maxdist = Math.max(maxdist, Math.max(Math.abs(m[0]-m[1]), Math.abs(m[2]-m[3])));
        }
        List<int[]> toRemove = new ArrayList<>();
        for (int i = 0; i < validMoves.size(); i++) {
            int[] currentArray = validMoves.get(i);
            for (int j = i + 1; j < validMoves.size(); j++) {
                if (isSpecificRotation(currentArray, validMoves.get(j))) {
                    // Mark currentArray for removal if a specific rotation is found
                    toRemove.add(currentArray);
                    break;
                }
            }
        }
        if(!twopart){
            for (int[] m : validMoves) {
                if (Math.abs(m[0] - m[1])!=maxdist) {
                    toRemove.add(m);
                }
            }
        }
        // Remove marked arrays from the main list
        validMoves.removeAll(toRemove);
        // Display valid moves
        System.out.println(Colour.getplayercolour(player)+ "\n━━━━━━━━━━━━━━━━━━━━━━━━Valid Moves:━━━━━━━━━━━━━━━━━━━━━━━━━━"+Colour.NONE.shader());
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
                    System.out.printf("%s) %s%n", getNextLabel(index), firstMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(!twopart && m[0] == m[1]){
                    System.out.printf("%s) %s%n", getNextLabel(index), secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(m[0] != m[1]&& m[2] != m[3]){
                    System.out.printf("         %s) %s, and then %s%n", getNextLabel(index), firstMove, secondMove);
                    addMoveToCollections(index, m);
                    index++;
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
                    System.out.printf("%s) %s%n", getNextLabel(index), firstMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(!twopart && m[0] == m[1]){
                    System.out.printf("%s) %s%n", getNextLabel(index), secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(m[0] != m[1]&& m[2] != m[3]){
                    System.out.printf("         %s) %s, and then %s%n", getNextLabel(index), firstMove, secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }
            }
        }
        return true;
    }


    /**
     * Generates the next label for a move based on the index.
     *
     * @param index the index of the move
     * @return the label for the move
     */
    private String getNextLabel(int index) {
    StringBuilder label = new StringBuilder();
    while (index >= 0) {
        int remainder = index % 25;
        char nextChar = (char) ('a' + remainder);
        if (nextChar >= 'q') {
            nextChar++;
        }
        label.insert(0, nextChar);
        index = index / 25 - 1;
    }
    return label.toString();
}


    /**
     * Adds a move to the collections of available moves and move map.
     *
     * @param index the index of the move
     * @param move  the move to be added
     */
    private void addMoveToCollections(int index, int[] move) {
        String moveLabel = getNextLabel(index);
        availablemoves.add(moveLabel);
        moveMap.put(moveLabel, move.clone());
    }

    /**
     * Checks if the given move command is valid.
     *
     * @param moveInput the move command input
     * @return true if the move command is valid, false otherwise
     */
    public Boolean isValidMoveCommand(String moveInput) {
            return availablemoves.contains(moveInput);

}
    /**
     * Performs a depth-first search with re-entry to explore valid moves.
     *
     * @param locs       the list of locations
     * @param dice1      the value of the first die
     * @param dice2      the value of the second die
     * @param validMoves the list of valid moves
     * @param player     the player making the move
     * @param move       the current move being explored
     * @param depth      the current depth of the search
     * @param usedDice   the number of dice used
     * @param barcount   the count of checkers on the bar
     */
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
        if (isLegalMove(player, -1, reentryTarget1, locs)) {
            List<Integer> newLocs = new ArrayList<>(locs);
            move[depth*2] = player==1 ? -1 : 24; // Set re-entry point as -1 for off-board
            move[depth*2+1] = reentryTarget1;
            newLocs.add(reentryTarget1); // Add the new position
            if(barcount ==0 ){
                dfs(newLocs, 0, dice2, validMoves, player, move, depth+1);
            }
            else{
                dfsWithReentry(locs, 0, dice2, validMoves, player, move, depth+1, usedDice+1, barcount);
            }
        }

        // Use dice2 for re-entry and explore the second move with dice1
        if (isLegalMove(player, -1, reentryTarget2, locs)) {
            List<Integer> newLocs = new ArrayList<>(locs);
            move[0] = player==1 ? -1 : 24; // Set re-entry point as -1 for off-board
            move[1] = reentryTarget2;
            newLocs.add(reentryTarget2); // Add the new position
            if(barcount ==0){
                dfs(newLocs, dice1, 0, validMoves, player, move, 1);
            }
            else{
                dfsWithReentry(locs, dice1, 0, validMoves, player, move, 1, 0, barcount);
            }
        }
    }



    /**
     * Performs a depth-first search to explore valid moves.
     *
     * @param locs       the list of locations
     * @param dice1      the value of the first die
     * @param dice2      the value of the second die
     * @param validMoves the list of valid moves
     * @param player     the player making the move
     * @param move       the current move being explored
     * @param depth      the current depth of the search
     */
    private void dfs(List<Integer> locs, int dice1, int dice2, List<int[]> validMoves, int player, int[] move, int depth) {
        // If both moves are used, add the move to valid moves and return
        if (depth == 2) {
            validMoves.add(move.clone());
            return;
        }

        int direction = (player == 1) ? 1 : -1; // Direction multiplier based on player

        // Explore normal moves using remaining dice for all board positions in `locs`
        for (int loc : locs) {
            int target1 = loc + direction * dice1;
            int target2 = loc + direction * dice2;
            if (isLegalMove(player, loc, target1, locs)) {
                move[depth * 2] = loc;
                move[depth * 2 + 1] = target1;

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                if(board.getPoint(loc).getCount() == 1){
                    newLocs.remove(Integer.valueOf(loc)); // Remove the original position if only one counter on point
                }
                newLocs.add(target1); // Add the new position

                // Recursive DFS with incremented depth
                dfs(newLocs, 0, dice2, validMoves, player, move, depth + 1);
            }
            if (isLegalMove(player, loc, target2, locs)) {
                move[depth * 2] = loc;
                move[depth * 2 + 1] = target2;

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                if(board.getPoint(loc).getCount() == 1){
                    newLocs.remove(Integer.valueOf(loc)); // Remove the original position if only one counter on point
                }
                newLocs.add(target2); // Add the new position

                // Recursive DFS with incremented depth
                dfs(newLocs, dice1, 0, validMoves, player, move, depth + 1);
            }
        }
    }


    /**
     * Checks if a move is legal.
     *
     * @param player the player making the move
     * @param start  the starting point of the move
     * @param target the target point of the move
     * @param locs   the list of locations
     * @return true if the move is legal, false otherwise
     */
    private boolean isLegalMove(int player, int start, int target, List<Integer> locs) {

        if(board.bearoffcheck(player) && (target <0 || target>23)) {// Check if bearing off is allowed
            if(target == -1 || target == 24){
                return true;
            }
            int dist = start-target;
            List<Integer> higherRolls = new ArrayList<>();
            for (Integer loc : locs) {
                if ((player == 0 && loc > start) || (player == 1 && loc < start)) {
                    higherRolls.add(loc);
                }
            }for (Integer higherRoll : higherRolls) {
                if (isLegalMove(player, higherRoll, higherRoll - dist, locs)){
                    return false;
                }
            }

            int direction = (player == 0) ? 1 : -1;

            for (int i = start+direction; i != dist; i += direction) {
                if (board.getPointColor(i) == board.getPlayerColor(player)) {
                    return false;
                }
            }
            return true;
        }

        if(!board.bearoffcheck(player) && (target <0 || target>23)){
            return false;
        }


        if (start == -1 || start == 25) { // Check if it’s a re-entry move for players 1 and 0
            Colour targetColour = board.getPointColor(target);
            return targetColour == Colour.NONE || targetColour == board.getPlayerColor(player)
                    || board.getPointCount(target) == 1; // Allow re-entry if empty, same color, or only one opponent checker
        } else {
            // Regular legality check for non-re-entry moves
            if ((board.getPointColor(target) == board.getPlayerColor(player).returnopp()) && board.getPointCount(target) > 1) {
                return false;
            }
            return true;
        }
    }


    /**
     * Retrieves the move associated with the given command.
     *
     * @param command the move command
     * @return the move associated with the command, or null if not found
     */
    public int[] getMoveFromCommand(String command) {
        return moveMap.getOrDefault(command, null); // Return the associated move or null if not found
    }

    /**
     * Executes a move from the starting point to the target point.
     *
     * @param from the starting point of the move
     * @param to   the target point of the move
     */
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
            Point destination = board.getPoint(to);
            moveToPoint(sColour, destination);
        }

        // Proceed with moving to the destination
        else if (to >= 0 && to <= 23) {
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

            Point destination = board.getPoint(to);
            moveToPoint(sColour, destination);
        } else {
            // If `to` is outside 0-23, it indicates bearing off
            moveToEnd(from);
        }
    }


    /**
     * Moves a checker to the end point.
     *
     * @param index the index of the point from which the checker is moved
     */
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

    public void moveToPoint(Colour sColour, Point destination){
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
    }


    /**
     * Checks if two arrays are specific rotations of each other.
     *
     * @param arr1 the first array
     * @param arr2 the second array
     * @return true if the arrays are specific rotations of each other, false otherwise
     */
    private static boolean isSpecificRotation(int[] arr1, int[] arr2) {
        if (arr1.length != 4 || arr2.length != 4) return false;
        // Check if arr2 is [x3, x4, x1, x2] of arr1
        return arr1[0] == arr2[2] && arr1[1] == arr2[3] &&
                arr1[2] == arr2[0] && arr1[3] == arr2[1];
    }


    /**
     * Sets the board for the MoveHandler.
     *
     * @param board the board to be set
     */
    public void setBoard(Board board){
        this.board = board;
    }

}
