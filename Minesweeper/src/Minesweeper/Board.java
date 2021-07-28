package Minesweeper;

import java.util.*;

/**
 * Minesweeper
 *
 * Class Board:
 * Defines the board and track the state of the grid of cells.
 * Provides interfaces for interaction with the board.
 *
 * @author Peter Mitchell
 * @version 2021.1
 */
public class Board {
    /**
     * The 2d grid of cells that can be bombs or represent the number of nearby bombs.
     */
    private Cell[][] cells;
    /**
     * Number of cells on the horizontal axis.
     */
    private int width;
    /**
     * Number of cells on the vertical axis.
     */
    private int height;
    /**
     * Number of bombs currently on the board.
     */
    private int bombCount;
    /**
     * Number of cells that have been revealed.
     */
    private int revealedTotal;
    /**
     * Number of cells marked with a flag to block revealing as they are suspected to be bombs.
     */
    private int flaggedCount;

    /**
     * Creates a board with the specific width and height.
     * All cells are populated default cells (no bombs).
     *
     * @param width Number of cells on the horizontal axis.
     * @param height Number of cells on the vertical axis.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        bombCount = 0;
        cells = new Cell[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
            }
        }
        revealedTotal = 0;
        flaggedCount = 0;
    }

    /**
     * Prints out the board with formatted colours and numbering around the sides.
     */
    public void printBoard() {
        // Print the cells for the board
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(cells[x][y].getColouredString() + "  ");
            }
            // Print the numbering for this row at the end
            System.out.println(" |" + (y + 1));
        }
        // Filler line to make it look nicer
        for(int x = 0; x < width; x++) {
            System.out.print("_  ");
        }
        System.out.println();
        // Numbers for each column
        for(int x = 0; x < width; x++) {
            System.out.print((x+1) + " ");
            if(x+1 < 10)
                System.out.print(" ");
        }
        System.out.println();
    }

    /**
     * Print the current status including the total revealed cells, the total number of cells,
     * the number of bombs on the board, and the number of flagged cells.
     */
    public void printStatus() {
        System.out.println(revealedTotal + " revealed of " + (height*width)
                            + " with " + bombCount + " bombs! Flagged: " + flaggedCount);
    }

    /**
     * Gets if the cell at x, y is a bomb.
     * Assumes valid x and y.
     *
     * @param position A position with an x and y coordinate.
     * @return True if cell at x,y is a bomb.
     */
    public boolean isCellBomb(Position position) {
        return cells[position.x][position.y].getIsBomb();
    }

    /**
     * Gets if the cell at x, y is revealed.
     * Assumes valid x and y.
     *
     * @param position A position with an x and y coordinate.
     * @return True if cell at x,y is revealed.
     */
    public boolean isCellRevealed(Position position) {
        return cells[position.x][position.y].getIsRevealed();
    }

    /**
     * Gets if the cell at x, y is flagged.
     * Assumes valid x and y.
     *
     * @param position A position with an x and y coordinate.
     * @return True if cell at x,y is flagged.
     */
    public boolean isCellFlagged(Position position) { return cells[position.x][position.y].getIsFlagged(); }

    /**
     * Gets the cell located at the specified x and y coordinate.
     *
     * @param position A position with an x and y coordinate.
     * @return The cell object at the specified x and y coordinate.
     */
    public Cell getCellAt(Position position) {
        return cells[position.x][position.y];
    }

    /**
     * Reveals the cell at x,y.
     * If the cell has 0 bomb neighbours flood fill is
     * used to reveal all 0s adjacent and then all surrounding
     * numbers on the border.
     *
     * @param position A Position with an x and y coordinate.
     */
    public void revealCell(Position position) {
        if(cells[position.x][position.y].getNeighbours() != 0) {
            // Reveal the single cell
            revealedTotal++;
            cells[position.x][position.y].reveal();
        } else {
            // Flood fill reveal all cells adjacent with 0s.
            List<Position> revealedCells = floodFillReveal(position);
            // Reveal all cells bordering with the cells that were just revealed.
            List<Position> borderRevealedCells = revealAroundListOfPoints(revealedCells);
            revealedTotal += revealedCells.size() + borderRevealedCells.size();
        }
    }

    /**
     * Toggle the cell's flagged state at x,y.
     *
     * @param position A position with an x and y coordinate.
     */
    public void flagCell(Position position) {
        getCellAt(position).toggleIsFlagged();
        // Keep count of the number of flagged cells
        if(getCellAt(position).getIsFlagged()) flaggedCount++;
        else flaggedCount--;
    }

    /**
     * Spawn up to the maximum of maxBombs.
     * Maximum is not guaranteed if a cell is selected multiple times.
     * Assumes maxBombs is a lot lower than the total maximum cells.
     *
     * @param maxBombs The maximum possible number of bombs to spawn.
     */
    public void spawnBombs(int maxBombs) {
        Random rand = new Random();
        for(int i = 0; i < maxBombs; i++) {
            addBomb(new Position(rand.nextInt(width), rand.nextInt(height)));
        }
    }

    /**
     * Tests if all cells have been revealed except for bombs.
     *
     * @return True if won by revealing all cells excluding bombs.
     */
    public boolean isWon() {
        return revealedTotal + bombCount == width*height;
    }

    /**
     * Reveals all cells to make them visible.
     * Note that this reveal is forced and does not increase the number revealed
     * for the purpose of testing isWon(). Use for revealing at the end of a game.
     */
    public void revealAll() {
        for(int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[0].length; x++) {
                cells[x][y].reveal();
            }
        }
    }

    /**
     * Add a bomb at the specified x, and y.
     * This is aborted if the cell already contains a bomb.
     * All within 1 unit distance are notified they have a new neighbour.
     *
     * @param position A position with an x and y coordinate.
     * @return False if bomb at location already, true if bomb successfully placed.
     */
    private boolean addBomb(Position position) {
        // Already a bomb?
        if(getCellAt(position).getIsBomb()) return false;

        // Get bounds of cells +/- 1 around x,y
        int minX = Math.max(0,position.x-1);
        int maxX = Math.min(width-1,position.x+1);
        int minY = Math.max(0,position.y-1);
        int maxY = Math.min(height-1,position.y+1);
        // Increase neighbours for all surrounding cells
        for(int y1 = minY; y1 <= maxY; y1++) {
            for (int x1 = minX; x1 <= maxX; x1++) {
                cells[x1][y1].addNeighbour();
            }
        }

        getCellAt(position).setAsBomb();
        bombCount++;
        return true;
    }

    /**
     * Tests if the coordinate x and y are within the valid range.
     *
     * @param position A position with an x and y coordinate.
     * @return True if x and y are both in the valid board space.
     */
    public boolean validPosition(Position position)
    {
        return position.x >= 0 && position.y >= 0 && position.x < width && position.y < height;
    }


    /**
     * Flood fills starting at x,y revealing all cells with 0 neighbours that are adjacent.
     *
     * @param position A position with an x and y coordinate.
     * @return List of cells that were revealed.
     */
    private List<Position> floodFillReveal(Position position)
    {
        // Visiting array
        int[][] vis =new int[width][height];

        // Initialing all as zero
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                vis[x][y]=0;
            }
        }
        // List of points that have been flood filled to be returned
        List<Position> changedPoints =  new ArrayList<>();

        // Creating queue for breadth first search
        Queue<Position> positionQueue = new LinkedList<>();

        // Adds the selected position as the first to evaluate
        positionQueue.add(position);
        vis[position.x][position.y] = 1;

        // Until queue is empty
        while (!positionQueue.isEmpty())
        {
            // Extracting front position from the queue.
            Position positionToReveal = positionQueue.remove();
            getCellAt(positionToReveal).reveal();
            changedPoints.add(positionToReveal);

            // For Upside Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x+1,positionToReveal.y),vis,positionQueue);
            // For Downside Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x-1,positionToReveal.y),vis,positionQueue);
            // For Right side Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y+1),vis,positionQueue);
            // For Left side Pixel or Cell
            checkFloodFillToCell(new Position(positionToReveal.x,positionToReveal.y-1),vis,positionQueue);
        }
        return changedPoints;
    }

    /**
     * Helper function for floodFillReveal(). Tests one Cell at position p.
     * If the selected cell is valid coordinate, has not yet been visited,
     * and is a valid element to reveal for the flood fill of all cells with 0 neighbours.
     * Then it will add the cell as a new Cell to check neighbours of, and mark it as visited.
     *
     * @param position The position to check.
     * @param vis The visited matrix indicating Cells that have already been checked.
     * @param positionQueue The queue of Cell positions to still check.
     */
    private void checkFloodFillToCell(Position position, int[][] vis, Queue<Position> positionQueue) {
        if (validPosition(position)) {
            if (vis[position.x][position.y] == 0
                    && !getCellAt(position).getIsRevealed()
                    && getCellAt(position).getNeighbours() == 0) {
                positionQueue.add(position);
            }
            vis[position.x][position.y] = 1;
        }
    }

    /**
     * Takes a list of points and iterates through all coordinates to
     * perform a reveal of all cells up to 1 unit away from each cell.
     *
     * @param points List of points to reveal borders around.
     * @return List of revealed cells.
     */
    private List<Position> revealAroundListOfPoints(List<Position> points) {
        List<Position> changedCells = new ArrayList<>();
        for(Position p : points) {
            List<Position> revealedCells = revealAllAroundPoint(p);
            changedCells.addAll(revealedCells);
        }
        return changedCells;
    }

    /**
     * Reveals all cells up to one unit away from the position.
     * Will skip any cells that have 0 neighbouring bombs.
     *
     * @param position The position to reveal a border around.
     * @return List of revealed cells.
     */
    private List<Position> revealAllAroundPoint(Position position) {
        List<Position> changedCells = new ArrayList<>();

        // Get bounds of cells +/- 1 around x,y
        int minX = Math.max(0,position.x -1);
        int maxX = Math.min(width-1,position.x +1);
        int minY = Math.max(0,position.y -1);
        int maxY = Math.min(height-1,position.y +1);

        // Iterate through all surrounding cells
        for(int y1 = minY; y1 <= maxY; y1++) {
            for (int x1 = minX; x1 <= maxX; x1++) {
                // Not already revealed and not the start of another empty area.
                if(!cells[x1][y1].getIsRevealed() && cells[x1][y1].getNeighbours()>0) {
                    changedCells.add(new Position(x1,y1));
                    cells[x1][y1].reveal();
                }
            }
        }
        return changedCells;
    }
}
