package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A panel that renders a visual interactive version of a Board.
 * Accepts left mouse clicks to reveal cells.
 * Accepts right mouse clicks to flag cells.
 * After a left click the game state is updated and a victory/defeat
 * message is shown if the game is deemed to be over.
 */
public class BoardPanel extends JPanel implements MouseListener {
    /**
     * Reference to the Board containing the game state.
     */
    private Board board;
    /**
     * Represents the number of pixels wide each cell should render as.
     */
    private final int CELL_WIDTH = 32;
    /**
     * Represents the number of pixels tall each cell should render as.
     */
    private final int CELL_HEIGHT = 32;
    /**
     * The number of cells wide that the board has.
     */
    private int boardWidth;
    /**
     * The number of cells high that the board has.
     */
    private int boardHeight;

    /**
     * Represents a flag to indicate when a win/lose state occurs.
     * 0 = playing game (accepting input from clicks)
     * 1 = Game over.
     * 2 = Victory
     * A state greater than 0 will show an overlay indicating the state.
     */
    private int state = 0;

    /**
     * Font used for rendering the numbers in cells.
     */
    private Font font = new Font("Arial",Font.PLAIN,20);
    /**
     * Font used for rendering the end game text.
     */
    private Font endFont = new Font("Arial", Font.PLAIN, 30);

    /**
     * Constructs a board panel with appropriate size defined for the desired board size.
     * Sets the colour to gray. Attaches a mouseListener for left/right clicks.
     * Then creates a board with the defined size and spawns up to 10 bombs.
     *
     * @param boardWidth The desired number of cells horizontally.
     * @param boardHeight THe desired number of cells vertically.
     */
    public BoardPanel(int boardWidth, int boardHeight) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(CELL_WIDTH * boardWidth, CELL_HEIGHT * boardHeight));
        setBackground(Color.gray);
        addMouseListener(this);
        board = new Board(boardWidth, boardHeight);
        board.spawnBombs(10);
    }

    /**
     * Called once at the start and then is called every time repaint() is used.
     * Renders all the cells and their relevant content.
     * A grid is drawn with lines, yellow blocks represent flagged cells,
     * dark gray blocks represent unrevealed cells. Cells are otherwise
     * filled with a valid character if they are a bomb or have neighbouring bombs.
     * Finally an overlay is drawn if the game state has reached victory or defeat.
     *
     * @param g A reference to the graphics object for rendering.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(font);
        // Draws the grid to split up all cells.
        drawGrid(g);

        // Fill all the cells with relevant state visual data
        for(int x = 0; x < boardWidth; x++) {
            for(int y = 0; y < boardHeight; y++) {
                if(!board.isCellRevealed(x,y))
                    if(board.isCellFlagged(x,y))
                        drawFilledCell(g, x, y, Color.yellow);
                    else
                        drawFilledCell(g, x, y, Color.darkGray);
                else
                    drawCellContent(g,x,y);
            }
        }

        // Show an overlay for victory/defeat if game is over.
        if(state > 0)
            drawEndPopup(g);
    }

    /**
     * Draws the overlay when a victory or defeat is triggered.
     * Draws a rectangle with border sized to fit the end game message.
     * Then draws on top of it the victory or defeat message.
     *
     * @param g A reference to the Graphics object for rendering.
     */
    private void drawEndPopup(Graphics g) {
        g.setFont(endFont);
        g.setColor(Color.WHITE);
        String str = (state == 1) ? "Boom! You lose. :(" : "You win! :D";
        int textWidth = g.getFontMetrics().stringWidth(str);

        // Render the rectangle filled with while and a black border.
        g.fillRect(boardWidth*CELL_WIDTH/2-textWidth/2-10,boardHeight*CELL_HEIGHT/2-35,textWidth+20,50);
        g.setColor(Color.BLACK);
        g.drawRect(boardWidth*CELL_WIDTH/2-textWidth/2-10,boardHeight*CELL_HEIGHT/2-35,textWidth+20,50);

        // Overlay the text with either red or green colour based on the state (red for loss, green for victory).
        g.setColor((state == 1) ? Color.red : Color.GREEN);
        g.drawString(str, boardWidth*CELL_WIDTH/2-textWidth/2, boardHeight*CELL_HEIGHT/2);
    }

    /**
     * Draws a grid by drawing vertical lines and then horizontal lines based on the number of cells.
     *
     * @param g A reference to the Graphics object for rendering.
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = 0;
        int y1 = boardHeight * CELL_HEIGHT;
        for(int x = 0; x < boardWidth; x++)
            g.drawLine(x * CELL_WIDTH, y1, x * CELL_WIDTH, y2);

        // Draw horizontal lines
        int x2 = 0;
        int x1 = boardWidth * CELL_WIDTH;
        for(int y = 0; y < boardHeight; y++)
            g.drawLine(x1, y * CELL_HEIGHT, x2, y * CELL_HEIGHT);
    }

    /**
     * Translates a filled rect with specified colour to sit inside the space where
     * it should be relative to the board space.
     *
     * @param g A reference to the Graphics object for rendering.
     * @param x X coordinate on the Board.
     * @param y Y coordinate on the Board.
     * @param c Colour to fill the cell with.
     */
    private void drawFilledCell(Graphics g, int x, int y, Color c) {
        g.setColor(c);
        g.fillRect(x*CELL_WIDTH+3, y*CELL_HEIGHT+2, CELL_WIDTH-5, CELL_HEIGHT-5);
    }

    /**
     * Renders a single character at the relative position on the board
     * based on the content in the Board object at x,y. Does nothing if "0".
     *
     * @param g A reference to the Graphics object for rendering.
     * @param x X coordinate on the Board.
     * @param y Y coordinate on the Board.
     */
    private void drawCellContent(Graphics g, int x, int y) {
        Cell c = board.getCellAt(x,y);
        if(c.toString().equals("0")) return;
        g.setColor(getColourForCell(c));
        g.drawString(c.toString(), (int)((x) * CELL_WIDTH)+10 , (int)((y+0.5) * CELL_HEIGHT)+5 );
    }

    /**
     * Takes a cell and evaluates which colour should be used based on whether it is a
     * bomb, is flagged, or the number of neighbours.
     *
     * @param c A reference to the Cell to use for state information.
     * @return A colour based on the current state of the cell.
     */
    private Color getColourForCell(Cell c) {
        if(c.getIsBomb()) {
            if(c.getIsFlagged()) return Color.yellow;
            else return Color.red;
        } else {
            if (c.getNeighbours() == 0) return Color.black;
            else if (c.getNeighbours() == 1) return Color.blue;
            else if (c.getNeighbours() == 2) return Color.green;
            else if (c.getNeighbours() == 3) return Color.red;
            else if (c.getNeighbours() == 4) return new Color(128, 0, 128);
            else if (c.getNeighbours() == 5) return new Color(128, 0, 0);
            else if (c.getNeighbours() == 6) return new Color(0, 0, 128);
            else if (c.getNeighbours() == 7) return Color.pink;
        }
        return Color.black;
    }

    /**
     * Provides interaction with left click to reveal cells and right click to flag cells.
     * Does nothing if the game is won, or if the click position is off the board.
     *
     * @param e Event information passed about the MouseEvent.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if game is won/lost
        if(state != 0) return;

        // Check relative cell position to translate into coordinates for Board
        int x = e.getX() / CELL_WIDTH;
        int y = e.getY() / CELL_HEIGHT;

        // Debug message to show where was clicked with the relative cell coordinates
        //System.out.println("Clicked: " + e.getX() + " " + e.getY() + ": " + x + " " + y);

        // Check for invalid click off the board
        if(!board.validCoord(x,y)) return;

        // Check for a left click to reveal the target cell
        if(SwingUtilities.isLeftMouseButton(e) && !board.isCellFlagged(x,y)) {
            board.revealCell(x,y);
            // Update the game state if the game has been won/lost
            if(board.isWon()) {
                board.revealAll();
                state = 2;
            } else if(board.isCellBomb(x,y)) {
                board.revealAll();
                state = 1;
            }
            board.printStatus();
        } else if(SwingUtilities.isRightMouseButton(e)) {
            // Flag the target cell if it was a right click
            board.flagCell(x,y);
        }

        // Forces the game to render with changes
        repaint();
    }

    /**
     * Not used.
     * @param e Not used.
     */
    @Override
    public void mousePressed(MouseEvent e) {}
    /**
     * Not used.
     * @param e Not used.
     */
    @Override
    public void mouseReleased(MouseEvent e) {}
    /**
     * Not used.
     * @param e Not used.
     */
    @Override
    public void mouseEntered(MouseEvent e) {}
    /**
     * Not used.
     * @param e Not used.
     */
    @Override
    public void mouseExited(MouseEvent e) {}
}
