package Step_11;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    private int CELL_SIZE = 32;
    private Board board;

    public BoardPanel(int boardWidth, int boardHeight) {
        board = new Board(boardWidth, boardHeight);
        board.spawnBombs(10);
        setPreferredSize(new Dimension(CELL_SIZE*boardWidth, CELL_SIZE*boardHeight));
        setBackground(Color.gray);
    }
}
