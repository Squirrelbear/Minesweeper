package Step_1;

import java.util.Scanner;

public class Game {
    private Scanner scan;

    public Game() {
        scan = new Scanner(System.in);
    }

    public void startGame() {
        int num1, num2;
        System.out.println("Enter space separated X then Y coordinate: ");
        num1 = scan.nextInt();
        num2 = scan.nextInt();
        System.out.println("You entered: " + num1 + " " + num2);
    }
}
