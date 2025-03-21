package ticTacToe;

interface Board extends privateBoard {
    Position getPosition();
    Cell getCell();
    Result makeMove(Move move);

}
