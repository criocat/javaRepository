package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {
    private Board board;

    public Game(Board board) {
        this.board = board;
    }

    public int play(Player player1, Player player2) {
        Move move = null;
        Result res = Result.UNKNOWN;
        board.clear();
        while (true) {
            res = playerMove(player1);
            if (res != Result.UNKNOWN) {
                return getReturn(res, 1);
            }
            res = playerMove(player2);
            if (res != Result.UNKNOWN) {
                return getReturn(res, 2);
            }
        }
    }

    public void tournament(ArrayList<Player> players) {
        int n = players.size();
        int cycle = getPowerOf2(n) + 1;
        int qualifyingRoundSize = (n - (1 << getPowerOf2(n))) * 2;
        Random rand = new Random();
        int[] curWinners = playQualRound(players, qualifyingRoundSize, n, rand, cycle + 1);
        while (curWinners.length != 1) {
            n = curWinners.length;
            curWinners = playCycle(players, n, rand, curWinners, cycle);
            --cycle;
        }
        System.out.println("absolute winner is player " + (curWinners[0] + 1));
    }

    private int[] playQualRound(ArrayList<Player> players, int qualifyingRound, int n, Random rand, int cycle) {
        int[] preWinners = new int[qualifyingRound];
        for (int i = 0; i < qualifyingRound; ++i) {
            preWinners[i] = i;
        }
        preWinners = playCycle(players, qualifyingRound, rand, preWinners, cycle);
        int[] curWinners = new int[(1 << getPowerOf2(n))];
        for (int i = 0; i < preWinners.length; ++i) {
            curWinners[i] = preWinners[i];
        }
        for (int i = preWinners.length; i < curWinners.length; ++i) {
            curWinners[i] = preWinners.length + i;
        }
        return curWinners;
    }

    private int[] playCycle(ArrayList<Player> players, int n, Random rand, int[] curWinners, int cycle) {
        int[] pos = getRandomPositions(n, rand);
        int[] winners = new int[n / 2];
        for (int i = 0; i < n / 2; i++) {
            int res = 0;
            int numPlayer1 = curWinners[pos[2 * i]];
            int numPlayer2 = curWinners[pos[2 * i + 1]];
            System.out.println("player " + (numPlayer1 + 1) + " vs player " + (numPlayer2 + 1));
            while (res == 0) {
                board.clear();
                res = play(players.get(numPlayer1), players.get(numPlayer2));
                if (res == 0) {
                    System.out.println("draw");
                }
            }
            winners[i] = curWinners[pos[2 * i + res - 1]];
            System.out.println("player " + (res == 1 ? numPlayer2 + 1 : numPlayer1 + 1) + " takes " + cycle + " place");
        }
        curWinners = winners;
        return curWinners;
    }

    private static int getPowerOf2(int n) {
        int cnt = 0;
        while (n > 1) {
            cnt++;
            n /= 2;
        }
        return cnt;
    }

    private static int[] getRandomPositions(int n, Random rand) {
        int[] pos = new int[n];
        boolean[] isUse = new boolean[n];
        Arrays.fill(isUse, false);
        for (int i = 0; i < n; ++i) {
            int randPos = Math.abs(rand.nextInt()) % (n - i);
            int cnt = 0;
            int curPos = -1;
            while (cnt != randPos + 1) {
                ++curPos;
                if (!isUse[curPos]) {
                    ++cnt;
                }
            }
            pos[i] = curPos;
            isUse[curPos] = true;
        }
        return pos;
    }

    private static int getReturn(Result res, int NumberOfPlayer) {
        if (res == Result.DRAW) {
            return 0;
        } else if (res == Result.LOSE) {
            return 3 - NumberOfPlayer;
        }
        return NumberOfPlayer;
    }

    private Result playerMove(Player player) {
        Move move = null;
        try {
            move = player.makeMove(board.getPosition());
            if (!board.isValid(move)) {
                return Result.LOSE;
            }
        } catch (Exception e) {
            return Result.LOSE;
        }
        return board.makeMove(move);
    }
}
