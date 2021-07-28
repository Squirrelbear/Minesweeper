package Step_6;

public class Cell {
    private boolean isRevealed;
    private boolean isBomb;
    private int neighbours;

    public Cell() {
        resetCell();
    }

    public void resetCell() {
        isRevealed = false;
        isBomb = false;
        neighbours = 0;
    }

    public void reveal() {
        isRevealed = true;
    }

    public boolean getIsRevealed() {
        return isRevealed;
    }

    public void setAsBomb() {
        isBomb = true;
    }

    public boolean getIsBomb() {
        return isBomb;
    }

    public void addNeighbour() {
        neighbours++;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public String toString() {
        if(isRevealed) {
            if(isBomb) {
                return "B";
            } else {
                return ""+neighbours;
            }
        } else {
            return "*";
        }
    }
}
