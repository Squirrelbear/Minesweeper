package Step_2;

import java.util.Scanner;

public class Game {
    private Scanner scan;
    private Board board;

    public Game() {
        scan = new Scanner(System.in);
        board = new Board(10,10);
    }

    public void startGame() {
        board.printBoard();
        int num1, num2;
        System.out.println("Enter space separated X then Y coordinate: ");
        num1 = scan.nextInt();
        num2 = scan.nextInt();
        System.out.println("You entered: " + num1 + " " + num2);
        board.revealCell(num1, num2);
        board.printBoard();
    }
}
