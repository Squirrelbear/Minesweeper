package Step_10;

public class Cell {
    private boolean isRevealed;
    private boolean isBomb;
    private int neighbours;
    private boolean isFlagged;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";

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

    private String colourString(String str, String colourFlag) {
        return colourFlag + str + ANSI_RESET;
    }

    public String getColouredString() {
        String str = toString();
        if(isFlagged) {
            return colourString(str, ANSI_GREEN);
        } else if(isRevealed) {
            if(isBomb) {
                return colourString(str, ANSI_RED);
            } else if(neighbours == 0) {
                return colourString(str, ANSI_PURPLE);
            } else {
                return colourString(str, ANSI_YELLOW);
            }
        } else {
            return colourString(str, ANSI_BLUE);
        }
    }
}
