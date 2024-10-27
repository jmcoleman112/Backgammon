public class Dice {
    private int dice1;
    private int dice2;

    public Dice(){
        this.dice1 = -1;
        this.dice2 = -1;
    }

    public int[] getDiceValues(){
        int[] values = new int[2];
        values[0] = dice1;
        values[1] = dice2;
        return values;
    }

    public void firstRoll(){
        dice1 = (int) (Math.random() * 6) + 1;
    }

    public void rollDice() {
        dice1 = (int) (Math.random() * 6) + 1;
        dice2 = (int) (Math.random() * 6) + 1;
    }

}
