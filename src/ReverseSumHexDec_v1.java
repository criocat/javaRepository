//import java.io.FileOutputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//import java.lang.Math;
//import java.util.Arrays;
//public class ReverseSumHexDec_v1 {
//
//    static class IntArray{
//        private int maxLen = 1;
//        private int len = 0;
//        private int[] arr = new int[1];
//        public IntArray() {}
//        private void expand() {
//            maxLen *= 2;
//            arr = Arrays.copyOf(arr, maxLen);
//        }
//        public void push_back(int num) {
//            if (len == maxLen) {
//                expand();
//            }
//            arr[len++] = num;
//        }
//        public int get(int ind) {
//            return arr[ind];
//        }
//        public int len() {
//            return len;
//        }
//        public void set(int ind, int val) {
//            arr[ind] = val;
//        }
//    }
//
//    static class DoubleArray{
//        private int maxLen = 1;
//        private int len = 0;
//        private IntArray[] arr = new IntArray[1];
//        private void expand() {
//            maxLen *= 2;
//            arr = Arrays.copyOf(arr, maxLen);
//        }
//        public void push_back(IntArray curArr) {
//            if (len == maxLen) {
//                expand();
//            }
//            arr[len++] = curArr;
//        }
//        public IntArray get(int ind) {
//            return arr[ind];
//        }
//    }
//
//    private static int get(int i, int j, DoubleArray arr) {
//        if (i < 0 || j < 0 || i >= arr.len || j >= arr.get(i).len()) {
//            return 0;
//        }
//        return arr.get(i).get(j);
//    }
//
//    private static int getNumber(DoubleArray arr, String str) {
//        if (str.charAt(0) == '0') {
//            return Integer.parseUnsignedInt(str.substring(2, str.length()), 16);
//        }
//        else {
//            return Integer.parseInt(str);
//        }
//    }
//
//    public static void main(String[] args) {
//        MyScanner sc = new MyScanner(System.in);
//        DoubleArray arr = new DoubleArray();
//        int len = 0;
//        String curString = sc.nextLine();
//        while(curString != null) {
//            arr.push_back(new IntArray());
//            ++len;
//            MyScanner scStr = new MyScanner(curString);
//            String tempStr = scStr.next();
//            while (!tempStr.equals("")) {
//                arr.get(len - 1).push_back(getNumber(arr, tempStr));
//                tempStr = scStr.next();
//            }
//            curString = sc.nextLine();
//        }
//        int maxLen = -1;
//        for (int i = 0; i < len; ++i) {
//            maxLen = Math.max(maxLen, arr.get(i).len());
//        }
//        IntArray prefPost = new IntArray();
//        for (int i = 0; i < maxLen; ++i) {
//            prefPost.push_back(-1);
//        }
//        for (int i = 0; i < len; ++i) {
//            for (int j = 0; j < arr.get(i).len(); ++j) {
//                arr.get(i).set(j, get(i, j - 1, arr) + get(prefPost.get(j), j, arr) - get(prefPost.get(j), j - 1, arr) + arr.get(i).get(j));
//                prefPost.set(j, i);
//                System.out.print(arr.get(i).get(j));
//                System.out.print(' ');
//            }
//            System.out.print('\n');
//        }
//} }