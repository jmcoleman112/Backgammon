import java.util.Scanner;


public class InputHandler {
    private final Scanner scanner;

    public InputHandler(){
        this.scanner = new Scanner(System.in);
    }

    public String getInput(){
        return scanner.nextLine();
    }

    public boolean isHintCommand(String input){
        if(input == null) return false;
        return input.equalsIgnoreCase("hint"); }

    public boolean isQuitCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("Q");
    }

    public boolean isBoardCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("board");
    }

    public boolean isDoubleCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("double");}

    public boolean isfileCommand(String input) {
        if(input == null) return false;
        return input.length() >= 5 && input.substring(0, 5).equalsIgnoreCase("test ");}


    public boolean isRollCommand(String input) {
        if(input == null) return false;
        return input.length() >= 4 && input.substring(0, 4).equalsIgnoreCase("roll");
    }

    public boolean isrolltestcommand(String input) {
        if(input == null) return false;
        if (input.length() != 8) {
            return false;
        }
        try {
            int x = Integer.parseInt(input.substring(5, 6));
            int y = Integer.parseInt(input.substring(7, 8));
            return x >= 1 && x <= 6 && y >= 1 && y <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isrollOnetestcommand(String input) {
        if(input == null) return false;
        if (input.length() != 6) {
            return false;
        }
        try {
            int x = Integer.parseInt(input.substring(5, 6));
            return x >= 1 && x <= 6;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isPipCommand(String input) {
        if(input == null) return false;
        return input.equalsIgnoreCase("pip");}



    public void closeScanner(){
        scanner.close();
    }
}