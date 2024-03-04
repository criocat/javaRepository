package expression;



import java.util.Scanner;

import expression.generic.*;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("not enough arguments");
            return;
        }
        try {
            Object[][][] array = new GenericTabulator().tabulate(args[0], args[1], -2, 2, -2, 2, -2, 2);
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 5; ++j) {
                    for (int k = 0; k < 5; ++k) {
                        if (array[i][j][k] != null) {
                            System.out.println((i - 2) + " " + (j - 2) + " " + (k - 2) + ": " + array[i][j][k].toString());
                        } else {
                            System.out.println("null");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
