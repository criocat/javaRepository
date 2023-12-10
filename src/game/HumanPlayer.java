package game;

import java.util.Scanner;

public class HumanPlayer implements Player {
    private Scanner sc = null;

    public HumanPlayer(Scanner sc) {
        this.sc = sc;
    }

    private boolean isValidString(String str) {
        boolean good = str.charAt(0) != '0' && str.length() <= 9;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                good = false;
                break;
            }
        }
        return good;
    }

    public Move makeMove(Position position) {
        String StringRow = null, StringCol = null;
        boolean isValidMove = false;
        int row = -1, col = -1;
        while (!isValidMove) {
            StringRow = sc.next();
            StringCol = sc.next();
            if(isValidString(StringRow) && isValidString(StringCol)) {
                row = Integer.parseInt(StringRow) - 1;
                col = Integer.parseInt(StringCol) - 1;
                isValidMove = position.validPosition(row, col) && position.getCell(row, col) == Cell.E;
            }

        }
        return new Move(row, col);
    }
}
