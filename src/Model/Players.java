//===============================================================
//By Group 33: Jack Coleman (21207103) and Naoise Golden (21376026)
//GITHUB ID: jmcoleman112, NaoiseG
//================================================================

package Model;

import Controller.InputHandler;
import View.Colour;

public class Players {
    private final String[] players;
    private int currentPlayer;

    // Sets player names at start of game
    public Players(InputHandler inputHandler) {
        this.players = new String[2];

        //prompt user for names
        for(int i = 0; i <= 1; i++){
            System.out.print("Please enter the name for "+ Colour.getplayercolour(i) +"PLayer " + (i+1) + Colour.NONE.shader() + ": ");

            //accept whatever the user says as orrecnt
            players[i] = inputHandler.getInput();
        }
    }


    //set player names at start of game, but from file/string
    public Players(String player1Name, String player2Name) {
        this.players = new String[2];

        //mock the user interaction, but just accept string names
        System.out.println("Please enter the name for "+ Colour.getplayercolour(0) +"PLayer " + (1) + Colour.NONE.shader() + ": "+ player1Name);
        players[0] = player1Name;

        //mock the user interaction, but just accept string names
        System.out.println("Please enter the name for "+ Colour.getplayercolour(1) +"PLayer " + (2) + Colour.NONE.shader() + ": "+ player2Name);
        players[1] = player2Name;
    }


    //switch the current player
    public void switchPlayer(){
        currentPlayer = (currentPlayer + 1) % 2;
    }

    //get the curent active player
    public int getCurrentPlayer(){
        return currentPlayer;
    }


    //get the length of the current active player's name
    public int getCurrentLength(){return players[currentPlayer].length();}

    //determine who the current active player is
    public void setCurrentPlayer(int player){
        currentPlayer = player;
    }


    //get the name of the current active player (with correct color)
    public String getPlayerName(int player){
        return (player == 0) ? (Colour.RED.shader() + players[0]+Colour.NONE.shader()) : (Colour.BLUE.shader() + players[1]+Colour.NONE.shader());
    }
}
