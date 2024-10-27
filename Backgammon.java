public class Backgammon {
    private final String[] players = new String[2];
    private Board board;
    private Dice dice;

    public Backgammon() {
        this.board = new Board();
        this.dice = new Dice();
    }

    public static void main(String[] args){
        Backgammon game = new Backgammon();
        InputHandler inputHandler = new InputHandler();

        if(game.Welcome(inputHandler)){
            game.setPlayers(inputHandler);
            int[] rollValues;
            int player = 0; //Player one goes first
            while(true){ //Eventually check for win
                rollValues = game.processRoll(inputHandler, player);
                System.out.println(game.getPlayer(player) + " rolled a " + rollValues[0] + " and a " + rollValues[1]);
                player = (player == 0) ? 1 : 0;  // Toggle between 0 and 1
            }
        }


        inputHandler.closeScanner();
    }

    public String getPlayer(int player){
        return players[player];
    }

    public boolean Welcome(InputHandler inputHandler) { //Loop until game started
        System.out.println("=====================================");
        System.out.println("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman and Naoise Golden.");
        System.out.println("For instructions on how to play, please read the README file.");
        System.out.println("=====================================\n\n");
        System.out.println("Press Enter to start or Q to quit\n");

        return inputHandler.startMessage();
    }

    public void setPlayers(InputHandler inputHandler) { //
        for(int i = 0; i <= 1; i++){
            System.out.println("Please enter the name for PLayer " + (i+1) + ": ");
            players[i] = inputHandler.getInput();
            System.out.println("\nPlayer" + (i+1) + " name is " + players[i]);
            System.out.println("To confirm this name please press Enter");
            System.out.println("To change name please enter new name");
            while(true){
                String input = inputHandler.getInput();
                if(inputHandler.isEnterCommand(input)){
                    break;
                }
                else{
                    players[i] = input;
                    System.out.println("\nPlayer" + (i+1) + " name changed to " + players[i]);
                    System.out.println("To confirm this name please press Enter");
                    System.out.println("To change name please enter new name");
                }
            }
        }
        System.out.println(players[0] + " will play with the red pieces");
        System.out.println(players[1] + " will play with the black pieces\n\n");
    }

    public int[] processRoll(InputHandler inputHandler, int player) {
        Display.displayBoard(board);
        System.out.println(getPlayer(player) + "'s turn");
        System.out.print("Please Enter a Command: ");
        String userInput = inputHandler.getInput();
        if(inputHandler.isRollCommand(userInput)){
            dice.rollDice();
        }
        if(inputHandler.isQuitCommand(userInput)){
            System.out.println("Thank You for playing!");
            System.out.println("Quitting game\n\n");
            System.exit(0);
        }

        int[] values;
        values = dice.getDiceValues();
        return values;
    }
}
