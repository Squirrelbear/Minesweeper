package Minesweeper;

import java.util.*;

/**
 * Minesweeper
 * Author: Peter Mitchell (2021)
 *
 * Class Board:
 * Defines the board and track the state of the grid of cells.
 * Provides interfaces for interaction with the board.
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
        cells = new Cell[height][width];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[y][x] = new Cell();
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
        for(int y = 0; y < cells.length; y++) {
            for(int x = 0; x < cells[0].length; x++) {
                System.out.print(cells[y][x].getColouredString() + "  ");
            }
            // Print the numbering for this row at the end
            System.out.println(" |" + (y + 1));
        }
        // Filler line to make it look nicer
        for(int x = 0; x < cells[0].length; x++) {
            System.out.print("_  ");
        }
        System.out.println();
        // Numbers for each column
        for(int x = 0; x < cells[0].length; x++) {
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
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return True if cell at x,y is a bomb.
     */
    public boolean isCellBomb(int x, int y) {
        return cells[y][x].getIsBomb();
    }

    /**
     * Gets if the cell at x, y is revealed.
     * Assumes valid x and y.
     *
     * @param x X coordinate.
     * @param y y coordinate.
     * @return True if cell at x,y is revealed.
     */
    public boolean isCellRevealed(int x, int y) {
        return cells[y][x].getIsRevealed();
    }

    /**
     * Gets if the cell at x, y is flagged.
     * Assumes valid x and y.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return True if cell at x,y is flagged.
     */
    public boolean isCellFlagged(int x, int y) { return cells[y][x].getIsFlagged(); }

    /**
     * Gets the toString() representation of cell at x,y.
     * F for flagged, B for bomb, * for hidden, and 0-8 for number of neighbours
     * Assumes valid x and y.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return toString() representation of x,y
     */
    public String getCellString(int x, int y) {return cells[y][x].toString();}

    /**
     * Gets the width in number of cells on the board.
     *
     * @return The number of cells horizontally on the board.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height in number of cells on the board.
     *
     * @return The number of cells vertically on the board.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the cell located at the specified x and y coordinate.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return The cell object at the specified x and y coordinate.
     */
    public Cell getCellAt(int x, int y) {
        return cells[y][x];
    }

    /**
     * Reveals the cell at x,y.
     * If the cell has 0 bomb neighbours flood fill is
     * used to reveal all 0s adjacent and then all surrounding
     * numbers on the border.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void revealCell(int x, int y) {
        if(cells[y][x].getNeighbours() != 0) {
            // Reveal the single cell
            revealedTotal++;
            cells[y][x].reveal();
        } else {
            // Flood fill reveal all cells adjacent with 0s.
            List<Pair> revealedCells = floodFillReveal(x,y);
            // Reveal all cells bordering with the cells that were just revealed.
            List<Pair> borderRevealedCells = revealAroundListOfPoints(revealedCells);
            revealedTotal += revealedCells.size() + borderRevealedCells.size();
        }
    }

    /**
     * Toggle the cell's flagged state at x,y.
     *
     * @param x X coordinate.
     * @param y Y Coordinate.
     */
    public void flagCell(int x, int y) {
        cells[y][x].toggleIsFlagged();
        // Keep count of the number of flagged cells
        if(cells[y][x].getIsFlagged()) flaggedCount++;
        else flaggedCount--;
    }

    /**
     * Spawn up to the maximum of maxBombs.
     * Maximum is not guaranteed if a cell is selected multiple times.
     * Assumes maxBombs is a lot lower than the total maximum cells.
     *
     * @param maxBombs
     */
    public void spawnBombs(int maxBombs) {
        Random rand = new Random();
        for(int i = 0; i < maxBombs; i++) {
            addBomb(rand.nextInt(width), rand.nextInt(height));
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
                cells[y][x].reveal();
            }
        }
    }

    /**
     * Add a bomb at the specified x, and y.
     * This is aborted if the cell already contains a bomb.
     * All within 1 unit distance are notified they have a new neighbour.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return False if bomb at location already, true if bomb successfully placed.
     */
    private boolean addBomb(int x, int y) {
        // Already a bomb?
        if(cells[y][x].getIsBomb()) return false;

        // Get bounds of cells +/- 1 around x,y
        int minX = Math.max(0,x-1);
        int maxX = Math.min(cells[0].length-1,x+1);
        int minY = Math.max(0,y-1);
        int maxY = Math.min(cells.length-1,y+1);
        // Increase neighbours for all surrounding cells
        for(int y1 = minY; y1 <= maxY; y1++) {
            for (int x1 = minX; x1 <= maxX; x1++) {
                cells[y1][x1].addNeighbour();
            }
        }

        cells[y][x].setAsBomb();
        bombCount++;
        return true;
    }

    /**
     * Tests if the coordinate x and y are within the valid range.
     *
     * @param x X coordinate to test.
     * @param y Y coordinate to test.
     * @return True if x and y are both in the valid board space.
     */
    public boolean validCoord(int x, int y)
    {
        if (x < 0 || y < 0) {
            return false;
        }
        if (x >= width || y >= height) {
            return false;
        }
        return true;
    }


    /**
     * Flood fills starting at x,y revealing all cells with 0 neighbours that are adjacent.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return List of cells that were revealed.
     */
    private List<Pair> floodFillReveal(int x, int y)
    {
        // Visiting array
        int vis[][]=new int[height][width];

        // Initialing all as zero
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                vis[i][j]=0;
            }
        }
        // List of points that have been flood filled to be returned
        List<Pair> changedPoints =  new ArrayList<>();

        // Creating queue for bfs
        Queue<Pair> obj = new LinkedList<>();

        // Pushing pair of {x, y}
        Pair pq=new Pair(x,y);
        obj.add(pq);

        // Marking {x, y} as visited
        vis[y][x] = 1;

        // Until queue is empty
        while (!obj.isEmpty())
        {
            // Extracting front pair
            Pair coord = obj.peek();
            int x1 = coord.first;
            int y1 = coord.second;

            cells[y1][x1].reveal();
            changedPoints.add(coord);

            // Poping front pair of queue
            obj.remove();

            // For Upside Pixel or Cell
            checkFloodFillToCell(new Pair(x1+1,y1),vis,obj);
            // For Downside Pixel or Cell
            checkFloodFillToCell(new Pair(x1-1,y1),vis,obj);
            // For Right side Pixel or Cell
            checkFloodFillToCell(new Pair(x1,y1+1),vis,obj);
            // For Left side Pixel or Cell
            checkFloodFillToCell(new Pair(x1,y1-1),vis,obj);
        }
        return changedPoints;
    }

    /**
     * Helper function for floodFillReveal(). Tests one Cell at position p.
     * If the selected cell is valid coordinate, has not yet been visited,
     * and is a valid element to reveal for the flood fill of all cells with 0 neighbours.
     * Then it will add the cell as a new Cell to check neighbours of, and mark it as visited.
     *
     * @param p The position to check.
     * @param vis The visited matrix indicating Cells that have already been checked.
     * @param objQueue The queue of Cell positions to still check.
     */
    private void checkFloodFillToCell(Pair p, int[][] vis, Queue<Pair> objQueue) {
        if (validCoord(p.first, p.second) && vis[p.second][p.first] == 0
                && !cells[p.second][p.first].getIsRevealed() && cells[p.second][p.first].getNeighbours() == 0)
        {
            objQueue.add(p);
            vis[p.second][p.first] = 1;
        }
    }

    /**
     * Takes a list of points and iterates through all coordinates to
     * perform a reveal of all cells up to 1 unit away from each cell.
     *
     * @param points List of points to reveal borders around.
     * @return List of revealed cells.
     */
    private List<Pair> revealAroundListOfPoints(List<Pair> points) {
        List<Pair> changedCells = new ArrayList<>();
        for(Pair p : points) {
            List<Pair> revealedCells = revealAllAroundPoint(p);
            changedCells.addAll(revealedCells);
        }
        return changedCells;
    }

    /**
     * Reveals all cells up to one unit away from the position coord.
     * Will skip any cells that have 0 neighbouring bombs.
     *
     * @param coord The position to reveal a border around.
     * @return List of revealed cells.
     */
    private List<Pair> revealAllAroundPoint(Pair coord) {
        List<Pair> changedCells = new ArrayList<>();

        // Get bounds of cells +/- 1 around x,y
        int minX = Math.max(0,coord.first-1);
        int maxX = Math.min(cells[0].length-1,coord.first+1);
        int minY = Math.max(0,coord.second-1);
        int maxY = Math.min(cells.length-1,coord.second+1);

        // Iterate through all surrounding cells
        for(int y1 = minY; y1 <= maxY; y1++) {
            for (int x1 = minX; x1 <= maxX; x1++) {
                // Not already revealed and not the start of another empty area.
                if(!cells[y1][x1].getIsRevealed() && cells[y1][x1].getNeighbours()>0) {
                    changedCells.add(new Pair(x1,y1));
                    cells[y1][x1].reveal();
                }
            }
        }
        return changedCells;
    }
}
