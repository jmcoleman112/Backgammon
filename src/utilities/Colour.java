package utilities;

public enum Colour {
    RED("\033[0;31m"),    // red text color
    BLUE("\033[0;34m"), // Black text colo
    NONE ("\033[0m");    // No color

    private final String ansiCode;

    // Constructor to assign ANSI code to each color
    Colour(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public static String getplayercolour(int player){
        if (player == 0){
            return RED.shader();
        }
        if (player ==1){
            return BLUE.shader();
        }
        else{
            return NONE.shader();
        }
    }

    // Getter method to retrieve the ANSI code
    public String shader() {
        return ansiCode;
    }

    public Colour returnopp(){
        if (this == Colour.RED){
            return Colour.BLUE;
        }
        else{
            return Colour.RED;
        }
    }


}
