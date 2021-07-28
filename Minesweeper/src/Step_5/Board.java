package Step_5;

import java.util.Random;

public class Board {
    private Cell[][] cells;
    private int width, height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cells[x][y] = new Cell();
            }
        }
    }

    public void printBoard() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print(cells[x][y] + "  ");
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
        cells[position.x][position.y].reveal();
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
}
