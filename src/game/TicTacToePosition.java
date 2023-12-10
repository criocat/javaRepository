package game;

public class TicTacToePosition implements Position {
    private final Cell[][] cells;
    private final int n, m, boardType, d, r;
    private final Cell turn;

    public TicTacToePosition(Cell[][] cells, Cell turn, int boardType) {
        this.cells = cells;
        n = cells.length;
        m = cells[0].length;
        d = n;
        r = d / 2;
        this.turn = turn;
        this.boardType = boardType;
    }

    public int getCountRow() {
        return n;
    }

    public boolean validPosition(int row, int col) {
        if (boardType == 0) {
            return row >= 0 && col >= 0 && row < n && col < m;
        }
        else {
            boolean good = (0 <= row && row < d && 0 <= col && col < d);
            int dr = 2 * (row >= (d / 2) ? row - (d / 2) : ((d - 1) / 2) - row);
            int dc = 2 * (col >= (d / 2) ? col - (d / 2) : ((d - 1) / 2) - col);
            dr += (d % 2 == 0 ? 1 : -1);
            dc += (d % 2 == 0 ? 1 : -1);
            if (dr * dr + dc * dc > 4 * r * r) {
                good = false;
            }
            return good;
        }
    }

    public int getCountColumn() {
        return m;
    }

    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    public Cell getTurn() {
        return turn;
    }
}
