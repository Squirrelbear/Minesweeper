package Minesweeper;

import java.util.Scanner;

/**
 * Minesweeper
 * Author: Peter Mitchell (2021)
 *
 * Class Game:
 * Text based implementation of the game with a game loop.
 * The game ends when either all non-bomb cells have been successfully
 * revealed or when the player loses by revealing a bomb.
 */
public class Game {
    /**
     * Reference to the board object representing the game's state.
     */
    private Board board;
    /**
     * Reference to a shared Scanner object.
     */
    private Scanner scan;
    /**
     * The position where the player is making their next action.
     */
    private int targetX, targetY;

    /**
     * Initialises the game with a shared Scanner.
     * Creates a board with size 10,10 and spawns up to 10 bombs on it.
     */
    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
        board.spawnBombs(10);
    }

    /**
     * Handles the game loop swapping between printing the board and
     * handling input to reach the next state.
     * If a position is typed in followed by "flag" the target cell will be flagged.
     * Otherwise the position will be used to reveal that target cell.
     * Once the game reaches either a won state where all non-bombs
     * have been revealed or a bomb has been revealed the game ends.
     * When the game ends the full board is revealed and a victory/defeat message is shown.
     */
    public void startGame() {
        // Used to apply commands in addition to the coordinate.
        String extraCommand;
        do {
            board.printBoard();
            board.printStatus();
            getInput();
            extraCommand = getStringOrQuit(scan).trim();
            if(extraCommand.equalsIgnoreCase("flag")) {
                board.flagCell(targetX, targetY);
            } else if(board.isCellFlagged(targetX, targetY)) {
                System.out.println("You need to unflag that cell first.");
            } else {
                board.revealCell(targetX, targetY);
            }

            if(board.isWon()) {
                showFinalBoard();
                System.out.println("Victory! You revealed all the non-bombs!");
                return;
            }
        } while(extraCommand.equalsIgnoreCase("flag") || !board.isCellBomb(targetX,targetY));
        showFinalBoard();
        System.out.println("Boom! You hit a bomb! :(");
    }

    /**
     * Reveals all cells on the board and prints the board.
     * Use after the game has been won or lost to do a full reveal.
     */
    public void showFinalBoard() {
        board.revealAll();
        board.printBoard();
    }

    /**
     * Continues looping until a valid pair of integers have been entered.
     * Each of the two inputs must be valid and must be inside the play space on the board.
     * The values are stored into targetX and targetY with adjusted values
     * to take them from the 1 to n range to the 0 to n-1 range.
     */
    public void getInput() {
        do {
            System.out.println("Enter space separated X (bottom) then Y (left) coordinate: ");
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid X coordinate.");
                continue;
            }
            targetX = scan.nextInt();
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid Y coordinate.");
                continue;
            }
            targetY = scan.nextInt();
            targetX--;
            targetY--;
        } while(!isInputValid(targetX, targetY));
    }

    /**
     * Tests if the specified x and y coordinate are both inside the valid play space.
     * Then also checks if the cell has already been revealed. Can not reveal a cell that
     * has already been revealed.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return True if the coordinate is inside the play space and the cell has not been revealed.
     */
    private boolean isInputValid(int x, int y) {
        if(x < 0 || y < 0 || x > board.getWidth()-1 || y > board.getHeight()-1) {
            System.out.println("Coordinate not inside the play space!");
            return false;
        }
        if(board.isCellRevealed(x,y)) {
            System.out.println("That cell is already revealed!");
            return false;
        }
        return true;
    }

    /**
     * Helper method that reads in a line of text and checks if the text is "quit".
     * If the text is "quit" it will exit the game. Otherwise returns the input.
     *
     * @param scan Reference to shared Scanner object.
     * @return The input from nextLine() if the input was not "quit".
     */
    public static String getStringOrQuit(Scanner scan) {
        String input = scan.nextLine();
        if(input.equalsIgnoreCase("quit")) {
            System.out.println("Thanks for playing! Goodbye!");
            System.exit(0);
        }
        return input;
    }
}
