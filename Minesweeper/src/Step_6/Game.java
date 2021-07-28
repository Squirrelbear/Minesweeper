package Step_6;

import java.util.Scanner;

public class Game {
    private Scanner scan;
    private Board board;

    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
        board.spawnBombs(10);
        board.revealAll();
    }

    public void startGame() {
        Position inputPosition;
        do {
            board.printBoard();
            inputPosition = getPositionInput();
            board.revealCell(inputPosition);
        } while(true);
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
