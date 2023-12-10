package game;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(new TicTacToeBoard(10, 3));
        Scanner sc = new Scanner(System.in);
        System.out.println(game.play(new HumanPlayer(sc), new HumanPlayer(sc)));
    }
}
