package Step_17;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String[] inputOptions = { "GUI", "CLI", "Quit" };
        int choice = JOptionPane.showOptionDialog(null, "Choose how to play.",
                "GUI or CLI?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, inputOptions, null);

        if(choice == 0) {
            JFrame gui = new JFrame("Minesweeper");
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setResizable(false);
            gui.getContentPane().add(new BoardPanel(10, 10));
            gui.pack();
            gui.setVisible(true);
        } else if(choice == 1) {
            Game game = new Game();
            game.startGame();
        } else {
            System.exit(0);
        }
    }
}
