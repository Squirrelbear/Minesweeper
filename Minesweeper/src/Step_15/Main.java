package Step_15;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame gui = new JFrame("Minesweeper");
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setResizable(false);
        gui.getContentPane().add(new BoardPanel(10, 10));
        gui.pack();
        gui.setVisible(true);

        //Game game = new Game();
        //game.startGame();
    }
}
