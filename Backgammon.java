public class Backgammon {
    private final String[] players = new String[2];
    private final Board board;
    private final Dice dice;

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

            int player = game.decideFirstPlayer(inputHandler); //Player one goes first

            while(true){ //Eventually check for win
                Display.displayBoard(game.getBoard());
                rollValues = game.processRoll(inputHandler, player);
                Display.printDiceFace(rollValues[0], rollValues[1]);

                System.out.println(game.getPlayer(player) + " rolled a " + rollValues[0] + " and a " + rollValues[1]);
                player = (player == 0) ? 1 : 0;  // Toggle between 0 and 1
            }
        }
        inputHandler.closeScanner();
    }

    public String getPlayer(int player){ //Returns player names
        return players[player];
    }

    public Board getBoard(){
        return board;
    }

    public boolean Welcome(InputHandler inputHandler) { //Loop until game started
        System.out.println("=====================================");
        System.out.println("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman and Naoise Golden.");
        System.out.println("For instructions on how to play, please read the README file.");
        System.out.println("=====================================\n\n");
        System.out.println("Press Enter to start or Q to quit\n");

        return inputHandler.startMessage();
    }

    public void setPlayers(InputHandler inputHandler) { // Sets player names at start of game
        for(int i = 0; i <= 1; i++){
            System.out.println("Please enter the name for PLayer " + (i+1) + ": ");
            players[i] = inputHandler.getInput();
            System.out.println("\nPlayer" + (i+1) + " name is " + players[i]);
            }

        System.out.println(players[0] + " will play with the red pieces");
        System.out.println(players[1] + " will play with the black pieces\n\n");
    }

    public int[] processRoll(InputHandler inputHandler, int player) { //One roll per turn
        System.out.println(getPlayer(player) + "'s turn");
        System.out.print("Please Enter a Command: ");
        String userInput = inputHandler.getInput();

        int[] values;

        while(true){
            if(inputHandler.isRollCommand(userInput)){
                dice.rollDice();
                break;
            }
            if(inputHandler.isQuitCommand(userInput)){
                System.out.println("Thank You for playing!");
                System.out.println("Quitting game\n\n");
                System.exit(0);
                break;
            }
            else{
                System.out.println("Please enter a valid Command: ");
                userInput = inputHandler.getInput();
            }
        }


        values = dice.getDiceValues();
        return values;
    }

    public int decideFirstPlayer(InputHandler inputHandler) {
        int[] roll1 = new int[2], roll2 = new int[2];

        int first = 0;

        while(roll1[0] == roll2[0]){
            System.out.println("Players must roll the dice to see who goes first");
            roll1 = processRoll(inputHandler, 0);
            Display.printDiceFace(roll1[0], -1);
            System.out.println(getPlayer(0) + " rolled a " + roll1[0]);

            roll2 = processRoll(inputHandler, 1);
            Display.printDiceFace(roll2[0], -1);
            System.out.println(getPlayer(1) + " rolled a " + roll2[0]);

            if(roll1[0] != roll2[0]){
                first = (roll1[0] > roll2[0] ? 0 : 1); // Higher roll goes first
                System.out.println(getPlayer(first) + " rolled higher and will go first\n");
                break;
            }
            System.out.println("Rolls were equal!");
        }
        return first;
    }
}
