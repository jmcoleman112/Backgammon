import utilities.Colour;

public class MoveHandler {//Class to check and execute moves
    private final Board board;

    public MoveHandler(Board board) {
        this.board = board;
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
