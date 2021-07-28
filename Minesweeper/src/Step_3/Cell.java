package Step_3;

public class Cell {
    private boolean isRevealed;

    public Cell() {
        isRevealed = false;
    }

    public void reveal() {
        isRevealed = true;
    }

    public boolean getIsRevealed() {
        return isRevealed;
    }

    public String toString() {
        if(isRevealed) {
            return "1";
        } else {
            return "*";
        }
    }
}
