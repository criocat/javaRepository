package game;

public interface Board  {
    Cell getCell();
    Position getPosition();
    Result makeMove(Move move);
    boolean isValid(Move move);
    void clear();
}
