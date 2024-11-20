import utilities.Colour;

public class Players {
    private final String[] players;


    public Players() { // Sets player names at start of game
        this.players = new String[2];

        for(int i = 0; i <= 1; i++){
            InputHandler inputHandler = new InputHandler();
            System.out.print("Please enter the name for "+ Colour.getplayercolour(i) +"PLayer " + (i+1) + Colour.NONE.shader() + ": ");
            players[i] = inputHandler.getInput();
        }
    }

    public Players(String player1Name, String player2Name) {
        this.players = new String[2];

        System.out.println("Please enter the name for "+ Colour.getplayercolour(0) +"PLayer " + (1) + Colour.NONE.shader() + ": "+ player1Name);
        players[0] = player1Name;

        System.out.println("Please enter the name for "+ Colour.getplayercolour(1) +"PLayer " + (2) + Colour.NONE.shader() + ": "+ player2Name);
        players[1] = player2Name;
    }



    public String getPlayerName(int player){
        return (player == 0) ? (Colour.RED.shader() + players[0]+Colour.NONE.shader()) : (Colour.BLUE.shader() + players[1]+Colour.NONE.shader());
    }
}
