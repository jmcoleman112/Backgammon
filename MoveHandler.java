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

    public void executeMove(int from, int to){

        Point source = board.getPoint(from - 1);
        Colour sColour = source.getColor();

        Point destination = board.getPoint(to - 1);
        Colour dColour = destination.getColor();

        if(source.getCount() == 0){
            System.out.println("Error point is empty\n\n");
            return;
        }

        source.setCount(source.getCount() - 1); //Decrement pip count
        if(source.getCount() == 0){ //No more pips on point
            source.setColor(Colour.NONE);
        }

        if (sColour == dColour){ //Same colour
            destination.setCount(destination.getCount() + 1); //Increment pip count
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
}
