package Step_14;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener {
    private int CELL_SIZE = 32;
    private Board board;

    public BoardPanel(int boardWidth, int boardHeight) {
        board = new Board(boardWidth, boardHeight);
        board.spawnBombs(10);
        setPreferredSize(new Dimension(CELL_SIZE*boardWidth, CELL_SIZE*boardHeight));
        setBackground(Color.gray);
        addMouseListener(this);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        int y2 = 0;
        int y1 = board.getHeight() * CELL_SIZE;
        for(int x = 0; x < board.getWidth(); x++) {
            g.drawLine(x * CELL_SIZE, y1, x * CELL_SIZE, y2);
        }

        int x2 = 0;
        int x1 = board.getWidth() * CELL_SIZE;
        for(int y = 0; y < board.getHeight(); y++) {
            g.drawLine(x1, y * CELL_SIZE, x2, y * CELL_SIZE);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        board.drawBoard(g, CELL_SIZE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Position mousePosition = new Position(e.getX()/CELL_SIZE, e.getY()/CELL_SIZE);
        if(!board.validPosition(mousePosition)) return;

        if(SwingUtilities.isLeftMouseButton(e) && !board.isCellFlagged(mousePosition)) {
            board.revealCell(mousePosition);
        } else if(SwingUtilities.isRightMouseButton(e)) {
            board.flagCell(mousePosition);
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
