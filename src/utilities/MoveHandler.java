/**
 * The utilities.MoveHandler class is responsible for checking and executing moves in the Backgammon game.
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
 * @see utilities.Board
 * @see utilities.Dice
 * @see utilities.InputHandler
 * @see utilities.MoveHandler
 * @see utilities.Match
 * @see utilities.Players
 * @see utilities.Display
 */

package utilities;
import java.util.*;


public class MoveHandler {//Class to check and execute moves
    private Board board;
    List<String> availablemoves = new ArrayList<>();
    private Map<String, int[]> moveMap = new HashMap<>();


    /**
     * Constructs a utilities.MoveHandler with the specified board.
     *
     * @param board the board to be used by the utilities.MoveHandler
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

        //checks which player is trying to make moves
        if (player == 1) {
            locs = board.getColoredPoints(Colour.BLUE);
        } else {
            locs = board.getColoredPoints(Colour.RED);
        }

        //initilizes array of valid moves, and array of each individual move
        List<int[]> validMoves = new ArrayList<>();
        int[] move = new int[4];

        // actual DFS logic
        if (board.getBar(player).getCount() > 0) {

            System.out.println("Player " + (player + 1) + " has checkers on the bar. ");
            //DFS with re-entry logic
            dfsWithReentry(locs, dice1, dice2, validMoves, player, move, 0, 0, board.getBar(player).getCount());

        } else {
            //DF without re-entry logic

            dfs(locs, dice1, dice2, validMoves, player, move, 0);
        }

        //Error Check for if no moves are available
        if(validMoves.isEmpty()) {
            System.out.println("No moves available");
            return false;
        }

        //Removing invalid moves based on two dice usage
        int index = 0;
        boolean twopart = false;
        int maxdist = 0;


        //iterates through every move generated
        for(int[] m: validMoves){

            if(m[0] != m[1] && m[2] != m[3]) {

                if(!((m[0] < 0 && m[1] < 0) || (m[0] > 23 && m[1] > 23) || (m[2] < 0 && m[3] < 0) || (m[2] > 23 && m[3] > 23))){
                    twopart = true;
                }
            }
            //checks which is the bigger dice roll
            maxdist = Math.max(maxdist, Math.max(Math.abs(m[0]-m[1]), Math.abs(m[2]-m[3])));
        }

        //removes rolls that are the same but reversed
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


        //removing moves that are not the max distance if only one dice
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

        //iterate through every move
        for (int[] m : validMoves) {

            //checking which player it is
            if (player == 0) {
                // Check if the first move is a re-entry or a regular/bearing-off move
                String firstMove = (m[0] == 24)
                        ? String.format("Re-enter from off-board to %d", m[1] + 1)
                        : (m[1] < 0 || m[1] > 23) // Allow bearing off on the first move
                        ? String.format("Bear off from Pt. %d", m[0] + 1)
                        : String.format("Move %d -> %d", m[0] + 1, m[1] + 1);

                // Check if the second move is a regular move or bearing-off
                String secondMove = (m[2] == 24)
                        ? String.format("Re-enter from off-board to %d", m[3] + 1)
                        : (m[3] < 0 || m[3] > 23) // Allow bearing off on the first move
                        ? String.format("Bear off from Pt. %d", m[2] + 1)
                        : String.format("Move %d -> %d", m[2] + 1, m[3] + 1);

                //checks if the move is a one part move
                if(!twopart && m[2]==m[3]){
                    System.out.printf("         %s) %s%n", getNextLabel(index), firstMove);
                    addMoveToCollections(index, m);
                    index++;
                }

                //checks if the move is a one part move
                else if(!twopart && m[0] == m[1]){
                    System.out.printf("         %s) %s%n", getNextLabel(index), secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }

                //print move if two part
                else if(m[0] != m[1]&& m[2] != m[3] && twopart){
                    System.out.printf("         %s) %s, and then %s%n", getNextLabel(index), firstMove, secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }
            } else {
                // Second player logic
                String firstMove = (m[0] == -1)
                        ? String.format("Re-enter from off-board to %d", 24 - m[1])
                        : (m[1] < 0 || m[1] > 23) // Allow bearing off on the first move
                        ? String.format("Bear off from Pt. %d", 24 - m[0])
                        : String.format("Move %d -> %d", 24 - m[0], 24 - m[1]);

                // Check if the second move is a regular move or bearing-off
                String secondMove = (m[2] == -1)
                        ? String.format("Re-enter from off-board to %d", 24 - m[3])
                        : (m[3] < 0 || m[3] > 23) // Allow bearing off on the first move
                        ? String.format("Bear off from Pt. %d", 24 - m[2])
                        : String.format("Move %d -> %d", 24 - m[2], 24 - m[3]);
                if(!twopart && m[2]==m[3]){
                    //if move is just one dice

                    System.out.printf("         %s) %s%n", getNextLabel(index), firstMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(!twopart && m[0] == m[1]){
                    //if move is just one dice

                    System.out.printf("         %s) %s%n", getNextLabel(index), secondMove);
                    addMoveToCollections(index, m);
                    index++;
                }
                else if(m[0] != m[1]&& m[2] != m[3] && twopart){
                    //if move uses two dice

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

    //generates what the correct label should be based on the index
    while (index >= 0) {
        int remainder = index % 25;
        char nextChar = (char) ('a' + remainder);
        //removes q for quit command to function
        if (nextChar >= 'q') {
            nextChar++;
        }

        //inserting label into string
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

        //stores the valid moves to move map and available moves
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

        //generates player direction
        int direction = (player == 1) ? 1 : -1;

        //checks if DFS should be over
        if (depth == 2) {
            validMoves.add(move.clone());
            return;
        }

        //decrements barcount
        barcount--;
        // Re-entry moves from -1 (off-board) for player 0 and player 1
        int reentryTarget1 = (player == 1 ? -1 : 24) + direction * dice1;
        int reentryTarget2 = (player == 1 ? -1 : 24) + direction * dice2;

        // Use dice1 for re-entry and explore the second move with dice2
        if (isLegalMove(player, -1, reentryTarget1, locs)) {
            List<Integer> newLocs = new ArrayList<>(locs);


            // Set re-entry point as -1 for off-board
            move[depth*2] = player==1 ? -1 : 24;
            move[depth*2+1] = reentryTarget1;

            // Add the new temporary position
            newLocs.add(reentryTarget1);
            if(barcount ==0 ){

                //apply regulat DFS if bar is empty
                dfs(newLocs, 0, dice2, validMoves, player, move, depth+1);
            }
            else{

                //apply DFS with rentry if bar is not empty
                dfsWithReentry(newLocs, 0, dice2, validMoves, player, move, depth+1, usedDice+1, barcount);
            }
        }

        // Use dice2 for re-entry and explore the second move with dice1
        if (isLegalMove(player, -1, reentryTarget2, locs)) {

            List<Integer> newLocs = new ArrayList<>(locs);

            // Set re-entry point as -1 for off-board
            move[depth*2] = player==1 ? -1 : 24;
            move[depth*2+1] = reentryTarget2;

            // Add the new position
            newLocs.add(reentryTarget2);
            if(barcount ==0){
                //does regular DFS if bar is empty
                dfs(newLocs, dice1, 0, validMoves, player, move, depth+1);
            }
            else{

                //does DFS with re-entry if bar is not empty
                dfsWithReentry(newLocs, dice1, 0, validMoves, player, move, depth+1, usedDice+1, barcount);
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

        // Direction multiplier based on player
        int direction = (player == 1) ? 1 : -1;

        // Explore normal moves using remaining dice for all board positions in locs
        for (int loc : locs) {

            //calculates next target location
            int target1 = loc + direction * dice1;
            int target2 = loc + direction * dice2;

            //if movee is legal, operate second DFS (with first dice used
            if (isLegalMove(player, loc, target1, locs)) {
                move[depth * 2] = loc;
                move[depth * 2 + 1] = target1;

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                if(board.getPoint(loc).getCount() == 1){
                    // Remove the original position if only one counter on point
                    newLocs.remove(Integer.valueOf(loc));
                }

                // Add the new position
                newLocs.add(target1);

                // Recursive DFS with incremented depth
                dfs(newLocs, 0, dice2, validMoves, player, move, depth + 1);
            }

            //if move is legal with second dice, operate second DFS
            if (isLegalMove(player, loc, target2, locs)) {
                move[depth * 2] = loc;
                move[depth * 2 + 1] = target2;

                // Create a new list of locations for the next depth
                List<Integer> newLocs = new ArrayList<>(locs);
                if(board.getPoint(loc).getCount() == 1){

                    // Remove the original position if only one counter on point
                    newLocs.remove(Integer.valueOf(loc));
                }

                // Add the new position
                newLocs.add(target2);

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

        //if counter doesnt move, is legal
        if(start==target){
            return true;
        }

        //if bearing off is allowed, check specific move is good
        if(board.bearoffcheck(player) && (target <0 || target>23)) {// Check if bearing off is allowed

            //if roll is exact perfect, its legal
            if(target == -1 || target == 24){
                return true;
            }

            //checks to ensure that no higher roll is legal
            int dist = start-target;
            List<Integer> higherRolls = new ArrayList<>();


            for (Integer loc : locs) {

                //gets all higher locs
                if ((player == 0 && loc > start) || (player == 1 && loc < start)) {
                    higherRolls.add(loc);
                }

                //if any higher roll is legal, return false
            }for (Integer higherRoll : higherRolls) {
                if (isLegalMove(player, higherRoll, higherRoll - dist, locs)){
                    return false;
                }
            }

            int direction = (player == 0) ? 1 : -1;
            //otherwise, only approve the highest legal bearing off implementation
            for (int i = start+direction; i != dist; i += direction) {
                if (locs.contains(i) && board.getPointColor(i) == board.getPlayerColor(player)) {
                    return false;
                }
            }
            return true;
        }

        //if bear off not allowed, u cant bear off (duh)
        if(!board.bearoffcheck(player) && (target <0 || target>23)){
            return false;
        }

    // Check if it’s a re-entry move for players 1 and 0
        if (start == -1 || start == 25) {
            Colour targetColour = board.getPointColor(target);

            // Allow re-entry if empty, same color, or only one opponent checker
            return targetColour == Colour.NONE || targetColour == board.getPlayerColor(player)
                    || board.getPointCount(target) == 1;
        }
        else {

            // Regular legality check for non-re-entry moves
            return (board.getPointColor(target) != board.getPlayerColor(player).returnopp()) || board.getPointCount(target) <= 1;
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
            int barIndex = (to < 12) ? 1 : 0;
            Bar bar = board.getBar(barIndex);

            //fail if noting on bar
            if (bar.getCount() == 0) {
                return;
            }

            // Set source color for re-entry
            sColour = (barIndex == 1) ? Colour.BLUE : Colour.RED;
            if(from != to) {

                // Remove one checker from the bar
                bar.setCount(bar.getCount() - 1);
                Point destination = board.getPoint(to);

                moveToPoint(sColour, destination);
            }
        }

        // Proceed with moving to the destination
        else if (to >= 0 && to <= 23) {

            // Normal move on the board
            Point source = board.getPoint(from);
            sColour = source.getColor();


            //fails if no counter to move
            if (source.getCount() == 0) {
                return;
            }

            // Decrement checker count on source point
            source.setCount(source.getCount() - 1);

            // checks if no more checkers on point
            if (source.getCount() == 0) {
                source.setColor(Colour.NONE);
            }

            //actually move checker
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

        //fails if no piece to move
        if(point.getCount() == 0){
            return;
        }
        //Add checker to end with right color
        End end;
        if(point.getColor() == Colour.RED){
            end = board.getEnd(0);
        }
        else {
            end = board.getEnd(1);
        }
        end.setCount(end.getCount() + 1);

        //Decrement checker count
        point.setCount(point.getCount() - 1);

        //if no more checkers to move, make color none
        if(point.getCount() == 0){
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
            if (dColour != Colour.NONE) {
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
        // Check if both values in the array are negative
        // Check if arr2 is [x3, x4, x1, x2] of arr1 or if they are identical
        return (arr1[0] == arr2[2] && arr1[1] == arr2[3] &&
                arr1[2] == arr2[0] && arr1[3] == arr2[1]) ||
                (Arrays.equals(arr1, arr2));
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
