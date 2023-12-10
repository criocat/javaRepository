package game;


public interface Position {
    Cell getCell(int r, int c);
    int getCountRow();
    int getCountColumn();
    Cell getTurn();
    boolean validPosition(int r, int c);
}
