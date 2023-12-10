package game;


public final class Move {
    private final int row;
    private final int column;

    public Move(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
