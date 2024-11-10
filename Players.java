import utilities.Colour;

public class Players {
    private final String[] players;

    public Players() { // Sets player names at start of game
        this.players = new String[2];

        for(int i = 0; i <= 1; i++){
            InputHandler inputHandler = new InputHandler();
            System.out.print("Please enter the name for PLayer " + (i+1) + ": ");
            players[i] = inputHandler.getInput();
            printPlayerName(i);
        }
    }

    public String getPlayerName(int player){
        return (player == 0) ? (Colour.RED.shader() + players[0]) : (Colour.BLUE.shader() + players[1]);
    }


    public void printPlayerName(int player){
        System.out.println("Player" + (player+1) + " name is " + getPlayerName(player) + Display.resetColour() + "\n");
    }
}
