import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler(){
        this.scanner = new Scanner(System.in);
    }

    public String getInput(){
        return scanner.nextLine();
    }

    public boolean isEnterCommand(String input){
        return input.isEmpty();
    }

    public boolean isQuitCommand(String input) {
        return input.equalsIgnoreCase("Q");
    }

    public boolean isRollCommand(String input) {
        return input.equalsIgnoreCase("roll");
    }

    public boolean startMessage(){
        while(true){
            try {
                String input = getInput();
                if (isEnterCommand(input)) {
                    System.out.println("Starting game\n\n");
                    return true;
                } else if (isQuitCommand(input)) {
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

    public void closeScanner(){
        scanner.close();
    }
}