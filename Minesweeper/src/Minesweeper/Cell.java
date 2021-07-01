package Minesweeper;

/**
 * Minesweeper
 * Author: Peter Mitchell (2021)
 *
 * Class Cell:
 * Represents the state information for a single cell on
 * the minesweeper board. Identifies if the cell is a bomb,
 * is flagged, is revealed, and how many neighbouring bombs
 * there are nearby.
 */
public class Cell {
    /**
     * True if this cell is a bomb and show be avoided when revealing.
     */
    private boolean isBomb;
    /**
     * Indicates if the true value of this cell should be shown instead of a mystery.
     */
    private boolean isRevealed;
    /**
     * Indicates the number of nearby bombs in +/- 1 unit of this cell.
     * It is set by the nearby cells from a addNeighbour() call when a bomb is placed.
     */
    private int neighbours;
    /**
     * Indicates if the cell is flagged by the player. Usually because they suspect
     * the cell contains a bomb. This can be used to prevent accidental revealing.
     * isFlagged does not prevent reveal() and should be done by the system managing
     * the cell by checking getIsFlagged().
     */
    private boolean isFlagged;

    /**
     * Constructor to set Cell defaults.
     * Sets defaults by calling resetCell().
     */
    public Cell() {
        resetCell();
    }

    /**
     * Resets cell back to default with all variables set to false.
     * And number of neighbours is set to 0.
     */
    public void resetCell() {
        isBomb = false;
        isRevealed = false;
        neighbours = 0;
        isFlagged = false;
    }

    /**
     * Sets this cell as a bomb.
     */
    public void setAsBomb() {
        isBomb = true;
    }

    /**
     * Gets if this cell is a bomb.
     *
     * @return True if the cell is a bomb.
     */
    public boolean getIsBomb() {
        return isBomb;
    }

    /**
     * Sets the cell to be revealed.
     * This is NOT prevented by the isFlagged variable.
     * Use the getIsFlagged() before calling this to prevent
     * accidental revealing of flagged cells.
     */
    public void reveal() {
        isRevealed = true;
    }

    /**
     * Gets if this cell is revealed. Indicates that the cell's
     * true value should be shown instead of a mystery character.
     *
     * @return True if the cell is revealed.
     */
    public boolean getIsRevealed() {
        return isRevealed;
    }

    /**
     * Increases the number of neighbours with bombs nearby by one.
     * Should be called when a bomb is placed for neighbours to update the count.
     */
    public void addNeighbour() {
        neighbours++;
    }

    /**
     * Gets the number of nearby neighbours. 0 neighbours indicates that
     * there are no bombs in +/- 1 unit of this cell.
     * Can be a value between 0 to 8.
     *
     * @return Count of the number of nearby neighbours with bombs.
     */
    public int getNeighbours() {
        return neighbours;
    }

    /**
     * Gets if the cell has been flagged by the player.
     * This can be used to check before revealing to ensure accidental
     * reveals of bombs are not occurring.
     *
     * @return True if the cell is flagged.
     */
    public boolean getIsFlagged() {
        return isFlagged;
    }

    /**
     * Toggles the state of whether this cell is flagged between true and false.
     */
    public void toggleIsFlagged() {
        isFlagged = !isFlagged;
    }

    /**
     * Gets a single character representing the state of this cell.
     * B for bomb. F for flagged, * for mystery, or
     * 0 to 8 representing number of nearby neighbours.
     *
     * @return A single character representing the current state of the cell.
     */
    public String toString() {
        if(getIsRevealed()) {
            if(getIsBomb()) {
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

    /**
     * Uses the toString() method and then applies colours using
     * ANSI codes depending on the current state of the object.
     *
     * @return A single character with colour formatting applied.
     */
    public String getColouredString() {
        String str = toString();
        if(isFlagged) str = colourString(str, ANSI_GREEN);
        else if(isRevealed) {
            if(isBomb) str = colourString(str, ANSI_RED);
            else if(neighbours == 0) str = colourString(str, ANSI_PURPLE);
            else str = colourString(str, ANSI_YELLOW);
        } else {
            str = colourString(str, ANSI_BLUE);
        }
        return str;
    }

    /**
     * Helper method to apply colour to a specified String.
     * The colourFlag should be any valid ANSI colour code.
     * The colour is reset back to default after the String.
     *
     * @param str The String to apply colour to.
     * @param colourFlag The ANSI colour code to apply.
     * @return A coloured version of str assuming colouredFlag is a valid ANSI colour code.
     */
    private String colourString(String str, String colourFlag) {
        return colourFlag + str + ANSI_RESET;
    }

    // Definitions of the ANSI colour codes.
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
}
