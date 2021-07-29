package Step_15;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardPanel extends JPanel implements MouseListener {
    public enum GameState {Playing, Won, Lost}
    private GameState gameState;
    private int CELL_SIZE = 32;
    private Board board;

    public BoardPanel(int boardWidth, int boardHeight) {
        board = new Board(boardWidth, boardHeight);
        board.spawnBombs(10);
        setPreferredSize(new Dimension(CELL_SIZE*boardWidth, CELL_SIZE*boardHeight));
        setBackground(Color.gray);
        addMouseListener(this);
        gameState = GameState.Playing;
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

    private void drawEndPopup(Graphics g) {
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.setColor(Color.WHITE);
        String str;
        if(gameState == GameState.Won) {
            str = "You win! :D";
        } else {
            str = "Boom! You lose. :(";
        }
        int textWidth = g.getFontMetrics().stringWidth(str);
        g.fillRect(board.getWidth()*CELL_SIZE/2-textWidth/2-10,
                board.getHeight()*CELL_SIZE/2-35, textWidth+20, 50);
        g.setColor(Color.BLACK);
        g.drawRect(board.getWidth()*CELL_SIZE/2-textWidth/2-10,
                board.getHeight()*CELL_SIZE/2-35, textWidth+20, 50);
        if(gameState == GameState.Won) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.RED);
        }
        g.drawString(str, board.getWidth()*CELL_SIZE/2-textWidth/2-2, board.getHeight()*CELL_SIZE/2);
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
        board.drawBoard(g, CELL_SIZE);
        if(gameState != GameState.Playing) {
            drawEndPopup(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(gameState != GameState.Playing) return;

        Position mousePosition = new Position(e.getX()/CELL_SIZE, e.getY()/CELL_SIZE);
        if(!board.validPosition(mousePosition)) return;

        if(SwingUtilities.isLeftMouseButton(e) && !board.isCellFlagged(mousePosition)) {
            board.revealCell(mousePosition);
            if(board.isWon()) {
                board.revealAll();
                gameState = GameState.Won;
            } else if(board.isCellBomb(mousePosition)) {
                board.revealAll();
                gameState = GameState.Lost;
            }
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
