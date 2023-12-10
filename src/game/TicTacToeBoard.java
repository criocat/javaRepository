package game;

import java.util.Arrays;


public class TicTacToeBoard implements Board {
    private final int k, r, d, n, m;
    private Cell turn;
    private Cell[][] cells;
    private int[][][] drawTableX;
    private int[][][] drawTableO;
    private int sumX, sumO;
    private final int boardType;

    public TicTacToeBoard(int d, int k) {
        this.d = d;
        this.k = k;
        this.r = (d / 2);
        this.n = d;
        this.m = d;
        this.boardType = 1;
        clear();
    }

    public TicTacToeBoard(int n, int m, int k) {
        this.d = -1;
        this.r = -1;
        this.k = k;
        this.n = n;
        this.m = m;
        this.boardType = 0;
        clear();
    }

    public void clear() {
        turn = Cell.X;
        cells = new Cell[n][m];
        for (int i = 0; i < n; ++i) {
            Arrays.fill(cells[i], Cell.E);
        }
        drawTableX = new int[4][n][m];
        drawTableO = new int[4][n][m];
        sumX = initDrawTable(drawTableX);
        sumO = initDrawTable(drawTableO);
    }

    private final int[] dx = new int[]{1, 1, 1, 0};
    private final int[] dy = new int[]{-1, 0, 1, 1};

    private int initDrawTable(int[][][] drawTable) {
        int sum = 0;
        for (int dir = 0; dir < 4; ++dir) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < m; ++j) {
                    if (validPosition(i, j)) {
                        boolean good = true;
                        for (int h = 0; h < k; ++h) {
                            int newX = i + h * dx[dir];
                            int newY = j + h * dy[dir];
                            if (!validPosition(newX, newY)) {
                                good = false;
                                break;
                            }
                        }
                        drawTable[dir][i][j] += (good ? 1 : 0);
                        sum += (good ? 1 : 0);
                    }
                }
            }
        }
        return sum;
    }

    private int changeDrawTable(int[][][] drawTable, int x, int y) {
        int sum = 0;
        for (int dir = 0; dir < 4; ++dir) {
            if (validPosition(x, y)) {
                for (int h = 0; h < k; ++h) {
                    int newX = x - h * dx[dir];
                    int newY = y - h * dy[dir];
                    if (validPosition(newX, newY) && drawTable[dir][newX][newY] == 1) {
                        drawTable[dir][newX][newY] = 0;
                        sum += 1;
                    }
                }
            }
        }
        return sum;
    }

    private boolean validPosition(int row, int col) {
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

    private boolean checkWin(int r, int c, int dr, int dc) {
        int cnt = 0;
        for (int i = 0; validPosition(r + i * dr, c + i * dc); ++i) {
            if (cells[r + i * dr][c + i * dc] != turn) {
                break;
            }
            ++cnt;
        }
        for (int i = 1; validPosition(r - i * dr, c - i * dc); ++i) {
            if (cells[r - i * dr][c - i * dc] != turn) {
                break;
            }
            ++cnt;
        }
        return (cnt >= k);
    }

    public Cell getCell() {
        return turn;
    }

    public Result makeMove(Move move) {
        int c = move.getColumn(), r = move.getRow();
        cells[move.getRow()][move.getColumn()] = turn;
        Result res = Result.UNKNOWN;
        display();
        if (turn == Cell.X) {
            sumO -= changeDrawTable(drawTableO, c, r);
        } else {
            sumX -= changeDrawTable(drawTableX, c, r);
        }
        if (sumO == 0 && sumX == 0) {
            res = Result.DRAW;
        }
        if (checkWin(r, c, 1, 1) || checkWin(r, c, 1, -1) || checkWin(r, c, 0, 1) || checkWin(r, c, 1, 0)) {
            res = Result.WIN;
        }
        turn = (turn == Cell.X ? Cell.O : Cell.X);
        return res;
    }

    private void display() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                System.out.print((validPosition(i, j) ? cells[i][j].getField() : " ") + " ");
            }
            System.out.print('\n');
        }
    }

    public Position getPosition() {
        return new TicTacToePosition(cells, turn, boardType);
    }

    public boolean isValid(final Move move) {
        return validPosition(move.getRow(), move.getColumn()) && cells[move.getRow()][move.getColumn()] == Cell.E;
    }
}
