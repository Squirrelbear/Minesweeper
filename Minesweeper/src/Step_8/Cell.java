package Step_8;

public class Cell {
    private boolean isRevealed;
    private boolean isBomb;
    private int neighbours;
    private boolean isFlagged;

    public Cell() {
        resetCell();
    }

    public void resetCell() {
        isRevealed = false;
        isBomb = false;
        neighbours = 0;
        isFlagged = false;
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

    public boolean getIsFlagged() {
        return isFlagged;
    }

    public void toggleIsFlagged() {
        isFlagged = !isFlagged;
    }

    public String toString() {
        if(isRevealed) {
            if(isBomb) {
                return "B";
            } else {
                return ""+neighbours;
            }
        } else if(isFlagged) {
          return "F";
        } else {
            return "*";
        }
    }
}
