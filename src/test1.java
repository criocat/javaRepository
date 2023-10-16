import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.io.*;
public class test1 {

    public static int get1() {
    return get2();
    }
    public static int get2() {
        return 2;
    }

    public static void main(String[] args) {
        System.out.println((int)(System.lineSeparator().charAt(1)));
        System.out.print((int)'\r');
        System.out.print(' ');
    }
}
// 63 97