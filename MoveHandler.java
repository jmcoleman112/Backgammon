import utilities.Colour;

import java.util.ArrayList;
import java.util.List;

public class MoveHandler {//Class to check and execute moves
    private final Board board;
    private char[] availablemoves;
    private List<int[]> validMoves = new ArrayList<>();


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
        validMoves = new ArrayList<>();
        searchMoves(locs, dice1, dice2, validMoves);
        searchMoves(locs, dice2, dice1, validMoves);
        availablemoves = new char[validMoves.size()];
        char label = 'a';
        for (int i = 0; i < validMoves.size(); i++) {
            int[] move = validMoves.get(i);
            System.out.println(label + ": " + move[0] + " " + move[1]);
            availablemoves[i] = label;
            label++;
        }
    }

    public Boolean isValidMoveCommand(String moveInput) {
    for (char move : availablemoves) {
        if (move == moveInput.charAt(0)) {
            return true;
        }
    }
    return false;
}


    private void searchMoves(List<Integer> loc, int dice1, int dice2, List<int[]> validMoves) {
    if (dice1 == -1) {
        return;
    }
    int boardSize = 24; // Assuming there is a method to get the board size
    for (int i = 0; i < loc.size(); i++) {
        int currentLoc = loc.get(i);
        if (currentLoc + dice1 < boardSize && currentLoc + dice2 < boardSize) {
            Colour Lcolor = board.getPointColor(currentLoc);
            Colour OLcolor = Lcolor.returnopp();
            boolean dice1check = dice2 > 0 && (board.getPointColor(currentLoc + dice1) == OLcolor) && (board.getPointcount(currentLoc + dice1) > 1);
            boolean dice2check = dice2 > 0 && (board.getPointColor(currentLoc + dice2) == OLcolor) && (board.getPointcount(currentLoc + dice2) > 1);

            if (dice2check) {
                List<Integer> newLoc = new ArrayList<>(loc);
                newLoc.set(i, currentLoc + dice2);
                validMoves.add(new int[]{currentLoc + dice2, currentLoc});
                searchMoves(newLoc, dice1, -1, validMoves);
            } else if (dice1check) {
                List<Integer> newLoc = new ArrayList<>(loc);
                newLoc.set(i, currentLoc + dice1);
                validMoves.add(new int[]{currentLoc + dice1, currentLoc});
                searchMoves(newLoc, -1, -1, validMoves);
            }
        }
    }
}


    public int[] getMoveFromCommand(String moveInput){
    int index = moveInput.charAt(0) - 'a';
    return validMoves.get(index);
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
