package Minesweeper;

import javax.swing.*;

/**
 * Minesweeper
 *
 * Class Main:
 * Entry point for the program that creates the game and starts it.
 *
 * @author Peter Mitchell
 * @version 2021.1
 */
public class Main {
    /**
     * Entry point for the program. Lets the player choose if they would
     * like to play with either a GUI or CLI and then starts the game with
     * the selected option.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        String[] inputOptions = { "GUI", "CLI", "Quit" };
        int choice = JOptionPane.showOptionDialog(null, "Choose how to play.",
                                                "GUI or CLI?", JOptionPane.YES_NO_CANCEL_OPTION,
                                                  JOptionPane.PLAIN_MESSAGE, null, inputOptions, null);

        if(choice == JOptionPane.YES_OPTION) {
            // Launch the GUI version by creating the frame and making it visible.
            JFrame gui = new JFrame("Minesweeper");
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setResizable(false);
            gui.getContentPane().add(new BoardPanel(10,10));
            gui.pack();
            gui.setVisible(true);
        } else if(choice == JOptionPane.NO_OPTION) {
            // Launch the CLI version by creating the Game and then starting it.
            Game game = new Game();
            game.startGame();
        } else {
            // Quit was selected, so terminate the program.
            System.exit(0);
        }
    }
}
