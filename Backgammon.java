public class Backgammon {

    public boolean Welcome(InputHandler inputHandler) { //Loop until game started
        System.out.println("=====================================");
        System.out.println("Welcome to Backgammon! This is a implementation of Backgammon created by Jack Coleman and Naoise Golden.");
        System.out.println("For instructions on how to play, please read the README file.");
        System.out.println("=====================================\n\n");
        System.out.println("Press Enter to start or Q to quit\n");

        while(true){
            try {
                String input = inputHandler.getInput();
                if (inputHandler.isEnterCommand(input)) {
                    System.out.println("Starting game\n\n");
                    return true;
                } else if (inputHandler.isQuitCommand(input)) {
                    System.out.println("Quitting game\n\n");
                    return false;
                }
            }
            catch (Exception error){
                System.out.println("An error occurred: " + error.getMessage());
                return false;
            }
            System.out.println("Error: Please press Enter to start or Q to quit\n");
        }
    }

    public static void main(String[] args){
        Backgammon game = new Backgammon();
        InputHandler inputHandler = new InputHandler();
        Board board = new Board();
        Dice dice = new Dice();

        if(game.Welcome(inputHandler)){
            Display.displayBoard(board);
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
        }

        inputHandler.closeScanner();
    }
}
