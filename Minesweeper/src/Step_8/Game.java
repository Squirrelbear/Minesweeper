package Step_8;

import java.util.Scanner;

public class Game {
    private Scanner scan;
    private Board board;

    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
        board.spawnBombs(10);
    }

    public void startGame() {
        boolean isFlagging;
        Position inputPosition;
        do {
            board.printBoard();
            board.printStatus();
            inputPosition = getPositionInput();
            isFlagging = getStringOrQuit().equalsIgnoreCase("flag");
            if(isFlagging) {
                board.flagCell(inputPosition);
            } else if(board.isCellFlagged(inputPosition)) {
                System.out.println("You need to un-flag that cell first.");
            } else {
                board.revealCell(inputPosition);
            }
        } while(!board.isWon() && (isFlagging || !board.isCellBomb(inputPosition)));
        board.revealAll();
        board.printBoard();
        if(board.isWon()) {
            System.out.println("Victory! You revealed all the non-bombs!");
        } else {
            System.out.println("Boom! You hit a bomb! :(");
        }
    }

    public boolean isPositionInputValid(Position position) {
        if (!board.validPosition(position)) {
            System.out.println("Coordinate not inside the play space!");
            return false;
        }
        if(board.isCellRevealed(position)) {
            System.out.println("That cell is already revealed!");
            return false;
        }
        return true;
    }

    public Position getPositionInput() {
        Position input = new Position(0,0);
        do {
            System.out.println("Enter space separated X (bottom) then Y (right) coordinate: ");
            if(!scan.hasNextInt()) {
                getStringOrQuit();
                System.out.println("Invalid X coordinate.");
                continue;
            }
            input.x = scan.nextInt();
            if(!scan.hasNextInt()) {
                getStringOrQuit();
                System.out.println("Invalid Y coordinate.");
                continue;
            }
            input.y = scan.nextInt();
            input.x--;
            input.y--;
        } while(!isPositionInputValid(input));
        return input;
    }

    public String getStringOrQuit() {
        String input = scan.nextLine().trim();
        if(input.equalsIgnoreCase("quit")) {
            System.out.println("Thanks for playing!");
            System.exit(0);
        }
        return input;
    }
}
