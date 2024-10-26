package utilities;

public enum Colour {
    RED("\033[0;31m"),    // red text color
    BLACK("\033[0;30m"), // Black text colo
    NONE ("\033[0m");    // No color

    private final String ansiCode;

    // Constructor to assign ANSI code to each color
    Colour(String ansiCode) {
        this.ansiCode = ansiCode;
    }



    // Getter method to retrieve the ANSI code
    public String shader() {
        return ansiCode;
    }


}
