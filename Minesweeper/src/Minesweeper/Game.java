package Minesweeper;

import java.util.Scanner;

/**
 * Minesweeper
 *
 * Class Game:
 * Text based implementation of the game with a game loop.
 * The game ends when either all non-bomb cells have been successfully
 * revealed or when the player loses by revealing a bomb.
 *
 * @author Peter Mitchell
 * @version 2021.1
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
        Position inputPosition;
        do {
            board.printBoard();
            board.printStatus();
            inputPosition = getPositionInput();
            extraCommand = getStringOrQuit(scan).trim();
            if(extraCommand.equalsIgnoreCase("flag")) {
                board.flagCell(inputPosition);
            } else if(board.isCellFlagged(inputPosition)) {
                System.out.println("You need to un-flag that cell first.");
            } else {
                board.revealCell(inputPosition);
            }
        } while(!board.isWon() && (extraCommand.equalsIgnoreCase("flag") || !board.isCellBomb(inputPosition)));
        board.revealAll();
        board.printBoard();
        if(board.isWon()) {
            System.out.println("Victory! You revealed all the non-bombs!");
        } else {
            System.out.println("Boom! You hit a bomb! :(");
        }
    }

    /**
     * Continues looping until a valid pair of integers have been entered.
     * Each of the two inputs must be valid and must be inside the play space on the board.
     * The values are stored into targetX and targetY with adjusted values
     * to take them from the 1 to n range to the 0 to n-1 range.
     */
    public Position getPositionInput() {
        Position input = new Position(0,0);
        do {
            System.out.println("Enter space separated X (bottom) then Y (left) coordinate: ");
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid X coordinate.");
                continue;
            }
            input.x = scan.nextInt();
            if(!scan.hasNextInt()) {
                getStringOrQuit(scan);
                System.out.println("Invalid Y coordinate.");
                continue;
            }
            input.y = scan.nextInt();
            input.x--;
            input.y--;
        } while(!isPositionInputValid(input));
        return input;
    }

    /**
     * Tests if the specified x and y coordinate are both inside the valid play space.
     * Then also checks if the cell has already been revealed. Can not reveal a cell that
     * has already been revealed.
     *
     * @param position The position with an x and y coordinate.
     * @return True if the coordinate is inside the play space and the cell has not been revealed.
     */
    private boolean isPositionInputValid(Position position) {
        if(!board.validPosition(position)) {
            System.out.println("Coordinate not inside the play space!");
            return false;
        }
        if(board.isCellRevealed(position)) {
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
