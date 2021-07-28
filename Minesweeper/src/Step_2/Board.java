package Step_2;

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
                System.out.print(cells[x][y] + " ");
            }
            System.out.println();
        }
    }

    public void revealCell(int x, int y) {
        cells[x][y].reveal();
    }
}
