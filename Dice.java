public class Dice {
    private int dice1;
    private int dice2;

    public Dice(){
        this.dice1 = -1;
        this.dice2 = -1;
    }

    public void rollDice() {
        dice1 = (int) (Math.random() * 6) + 1;
        dice2 = (int) (Math.random() * 6) + 1;

        printDiceFace(dice1, dice2);
    }

    private static String[] getDiceFace(int number) {
        return switch (number) {
            case 1 -> new String[]{
                    "┌───────┐",
                    "│       │",
                    "│   ●   │",
                    "│       │",
                    "└───────┘"
            };
            case 2 -> new String[]{
                    "┌───────┐",
                    "│ ●     │",
                    "│       │",
                    "│     ● │",
                    "└───────┘"
            };
            case 3 -> new String[]{
                    "┌───────┐",
                    "│ ●     │",
                    "│   ●   │",
                    "│     ● │",
                    "└───────┘"
            };
            case 4 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│       │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            case 5 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│   ●   │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            case 6 -> new String[]{
                    "┌───────┐",
                    "│ ●   ● │",
                    "│ ●   ● │",
                    "│ ●   ● │",
                    "└───────┘"
            };
            default -> new String[]{
                    "Error: Invalid dice number."
            };
        };
    }

    public static void printDiceFace(int number1, int number2) {
        String[] dice1 = getDiceFace(number1);
        String[] dice2 = number2 == -1 ? new String[5] : getDiceFace(number2);

        for (int i = 0; i < 5; i++) {
            if (number2 == -1) {
                System.out.println(dice1[i]);
            } else {
                System.out.println(dice1[i] + "   " + dice2[i]);
            }
        }
    }
}
