package Step_4;

import java.util.Scanner;

public class Game {
    private Scanner scan;
    private Board board;

    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
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
            System.out.println("Enter space separated X then Y coordinate: ");
            input.x = scan.nextInt();
            input.y = scan.nextInt();
        } while(!isPositionInputValid(input));
        return input;
    }
}
