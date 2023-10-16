import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.lang.Math;
import java.util.Arrays;
public class ReverseSumHexDecAbc {

    static class IntArray{
        private int maxLen = 1;
        private int len = 0;
        private int[] arr = new int[1];
        public IntArray() {}
        private void expand() {
            maxLen *= 2;
            arr = Arrays.copyOf(arr, maxLen);
        }
        public void push_back(int num) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = num;
        }
        public int get(int ind) {
            return arr[ind];
        }
        public int len() {
            return len;
        }
        public void set(int ind, int val) {
            arr[ind] = val;
        }
    }

    static class DoubleArray {
        private int maxLen = 1;
        private int len = 0;
        private IntArray[] arr = new IntArray[1];

        private void expand() {
            maxLen *= 2;
            arr = Arrays.copyOf(arr, maxLen);
        }

        public void push_back(IntArray curArr) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = curArr;
        }

        public IntArray get(int ind) {
            return arr[ind];
        }
    }

    private static int get(int i, int j, DoubleArray arr) {
        if (i < 0 || j < 0 || i >= arr.len || j >= arr.get(i).len()) {
            return 0;
        }
        return arr.get(i).get(j);
    }

    private static int getNumber(String str) {
        int ind = (str.charAt(0) == '-' ? 1 : 0);
        if (str.charAt(0) == '0' && str.length() >= 2) {
            return Integer.parseUnsignedInt(str.substring(2, str.length()), 16);
        }
        else if(str.charAt(ind) >= '0' && str.charAt(ind) <= '9') {
            return Integer.parseInt(str);
        }
        else {
            char[] chars = new char[str.length()];
            if (ind == 1) {
                chars[0] = '-';
            }
            for (int i = ind; i < str.length(); ++i) {
                chars[i] = (char)(str.charAt(i) - 'a' + '0');
            }
            return Integer.parseInt(new String(chars));
        }
    }
    private static String getAbsNumber(int num) {
        String str = "" + num;
        int ind = (str.charAt(0) == '-' ? 1 : 0);
        char[] chars = new char[str.length()];
        for (int i = ind; i < str.length(); ++i) {
            chars[i] = (char)(str.charAt(i) + 'a' - '0');
        }
        if (ind == 1) {
            chars[0] = '-';
        }
        return new String(chars);
    }

    public boolean good(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }

    public static void main(String[] args) {
        MyScanner sc = new MyScanner(System.in);
        DoubleArray arr = new DoubleArray();
        int len = 0;
            String curString = sc.nextLine();
            while (curString != null) {
                arr.push_back(new IntArray());
                ++len;
                MyScanner scStr = new MyScanner(curString);
                String tempStr = scStr.next();
                while (tempStr != null) {
                    arr.get(len - 1).push_back(getNumber(tempStr));
                    tempStr = scStr.next();
                }
                curString = sc.nextLine();
            }
        int maxLen = -1;
        for (int i = 0; i < len; ++i) {
            maxLen = Math.max(maxLen, arr.get(i).len());
        }
        IntArray prefPost = new IntArray();
        for (int i = 0; i < maxLen; ++i) {
            prefPost.push_back(-1);
        }
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < arr.get(i).len(); ++j) {
                arr.get(i).set(j, get(i, j - 1, arr) + get(prefPost.get(j), j, arr) - get(prefPost.get(j), j - 1, arr) + arr.get(i).get(j));
                prefPost.set(j, i);
                System.out.print(getAbsNumber(arr.get(i).get(j)));
                System.out.print(' ');
            }
            System.out.print('\n');
        }
} }