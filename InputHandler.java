import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

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

//    public static void printDiceFace(int number) {
//        printDiceFace(number, -1);
//    }

    public void closeScanner(){
        scanner.close();
    }
}