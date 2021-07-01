package Minesweeper;

import javax.swing.*;

/**
 * Minesweeper
 * Author: Peter Mitchell (2021)
 *
 * Class GUI:
 * Initialises the entry point to create a JFrame
 * with a boardPanel and makes it visible.
 */
public class GUI extends JFrame {
    /**
     * Reference to the BoardPanel that contains a visual representation of Board.
     */
    private BoardPanel boardPanel;

    /**
     * Constructor for the GUI. Creates a JFrame, and a BoardPanel.
     */
    public GUI() {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardPanel = new BoardPanel(10,10);

        getContentPane().add(boardPanel);

        pack();
    }
}
