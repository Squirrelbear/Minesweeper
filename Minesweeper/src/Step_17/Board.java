package Step_17;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;

public class Board {
    private Cell[][] cells;
    private int width, height;
    private int bombCount, revealedTotal;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
            }
        }
        bombCount = 0;
        revealedTotal = 0;
    }

    public void printBoard() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(cells[x][y].getColouredString() + "  ");
            }
            System.out.println(" |" + (y+1));
        }
        for(int x = 0; x < width; x++) {
            System.out.print("_  ");
        }
        System.out.println();
        for(int x = 0; x < width; x++) {
            System.out.print((x+1) + " ");
            if(x+1 < 10) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    public void revealCell(Position position) {
        if(cells[position.x][position.y].getNeighbours() != 0) {
            revealedTotal++;
            cells[position.x][position.y].reveal();
        } else {
            List<Position> revealedCells = floodFillReveal(position);
            for(Position p : revealedCells) {
                revealAllAroundPoint(p);
            }
        }
    }

    public boolean validPosition(Position position) {
        return position.x >= 0 && position.y >= 0 && position.x < width && position.y < height;
    }

    public boolean isCellRevealed(Position position) {
        return cells[position.x][position.y].getIsRevealed();
    }

    public boolean isCellBomb(Position position) {
        return cells[position.x][position.y].getIsBomb();
    }

    public boolean isCellFlagged(Position position) {
        return cells[position.x][position.y].getIsFlagged();
    }

    public void flagCell(Position position) {
        cells[position.x][position.y].toggleIsFlagged();
    }

    public boolean addBomb(Position position) {
        if(isCellBomb(position)) {
            return false;
        }

        int minX = Math.max(0, position.x-1);
        int maxX = Math.min(width-1, position.x+1);
        int minY = Math.max(0,position.y-1);
        int maxY = Math.min(height-1, position.y+1);
        for(int y = minY; y <= maxY; y++) {
            for(int x = minX; x <= maxX; x++) {
                cells[x][y].addNeighbour();
            }
        }

        cells[position.x][position.y].setAsBomb();
        bombCount++;
        return true;
    }

    public void spawnBombs(int maxBombs) {
        Random rand = new Random();
        for(int i = 0; i < maxBombs; i++) {
            addBomb(new Position(rand.nextInt(width), rand.nextInt(height)));
        }
    }

    public void revealAll() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y].reveal();
            }
        }
    }

    public boolean isWon() {
        return revealedTotal + bombCount == width * height;
    }

    public void printStatus() {
        System.out.println(revealedTotal + " revealed of " + (width*height)
                            + " with " + bombCount + " bombs!");
    }

    private void revealAllAroundPoint(Position position) {
        int minX = Math.max(0, position.x-1);
        int maxX = Math.min(width-1, position.x+1);
        int minY = Math.max(0,position.y-1);
        int maxY = Math.min(height-1, position.y+1);
        for(int y = minY; y <= maxY; y++) {
            for(int x = minX; x <= maxX; x++) {
                if(!cells[x][y].getIsRevealed() && cells[x][y].getNeighbours() != 0) {
                    cells[x][y].reveal();
                    revealedTotal++;
                }
            }
        }
    }

    private void checkFloodFillToCell(Position position, boolean[][] vis, Queue<Position> positionQueue) {
        if(validPosition(position)) {
            if(!vis[position.x][position.y] && !isCellRevealed(position)
                    && cells[position.x][position.y].getNeighbours() == 0) {
                positionQueue.add(position);
            }
            vis[position.x][position.y] = true;
        }
    }

    private List<Position> floodFillReveal(Position position) {
        boolean[][] vis = new boolean[width][height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                vis[x][y] = false;
            }
        }

        List<Position> changedPoints = new ArrayList<>();
        Queue<Position> positionQueue = new LinkedList<>();
        positionQueue.add(position);
        vis[position.x][position.y] = true;

        while(!positionQueue.isEmpty()) {
            Position positionToReveal = positionQueue.remove();
            cells[positionToReveal.x][positionToReveal.y].reveal();
            revealedTotal++;
            changedPoints.add(positionToReveal);

            checkFloodFillToCell(new Position(positionToReveal.x+1, positionToReveal.y), vis, positionQueue);
            checkFloodFillToCell(new Position(positionToReveal.x-1, positionToReveal.y), vis, positionQueue);
            checkFloodFillToCell(new Position(positionToReveal.x, positionToReveal.y+1), vis, positionQueue);
            checkFloodFillToCell(new Position(positionToReveal.x, positionToReveal.y-1), vis, positionQueue);
        }
        return changedPoints;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawBoard(Graphics g, int cellSize) {
        g.setFont(new Font("Arial",Font.PLAIN,20));
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                cells[x][y].drawCell(g, x*cellSize, y*cellSize, cellSize);
            }
        }
    }

    public void reset() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                cells[x][y].resetCell();
            }
        }
        bombCount = 0;
        revealedTotal = 0;
    }
}
