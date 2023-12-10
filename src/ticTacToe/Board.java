package ticTacToe;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
interface Board extends privateBoard {
    Position getPosition();
    Cell getCell();
    Result makeMove(Move move);

}
