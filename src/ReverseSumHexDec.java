import java.util.Scanner;
import java.lang.Math;
public class ReverseSumHexDec {
    static class IntArray{
        int maxLen = 1;
        int len = 0;
        int[] arr = new int[1];
        public IntArray() {}
        private void expand() {
            maxLen *= 2;
            int[] newArr = new int[maxLen];
            for (int i = 0; i < arr.length; ++i) {
                newArr[i] = arr[i];
            }
            arr = newArr;
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
    }

    static class DoubleArray{
        int maxLen = 1;
        int len = 0;
        IntArray[] arr = new IntArray[1];
        private void expand() {
            maxLen *= 2;
            IntArray[] newArr = new IntArray[maxLen];
            for (int i = 0; i < arr.length; ++i) {
                newArr[i] = arr[i];
            }
            arr = newArr;
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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DoubleArray arr = new DoubleArray();
        int len = 0;
        while(sc.hasNextLine()) {
            arr.push_back(new IntArray());
            ++len;
            String curString = sc.nextLine();
            for (int j = 0; j < curString.length(); ++j) {
                if (!Character.isWhitespace(curString.charAt(j))) {
                    int pointerR = -1;
                    for (int k = j; k < curString.length() && !Character.isWhitespace(curString.charAt(k)); ++k) {
                        pointerR = k;
                    }
                    if (pointerR - j + 1 >= 2 && curString.charAt(j) == '0' && (curString.charAt(j + 1) == 'x' || curString.charAt(j + 1) == 'X')) {
                        arr.get(len - 1).push_back((int)Long.parseLong(curString.substring(j + 2, pointerR + 1), 16));
                    }
                    else {
                        arr.get(len - 1).push_back((int)Long.parseLong(curString.substring(j, pointerR + 1)));
                    }
                    j = pointerR;
                }
            }
        }

        DoubleArray prefSum = new DoubleArray();
        for (int i = 0; i < len; ++i) {
            prefSum.push_back(new IntArray());
            for (int j = 0; j < arr.get(i).len(); ++j) {
                int res = (j == 0 ? 0 : prefSum.get(i).get(j - 1)) + arr.get(i).get(j);
                prefSum.get(i).push_back(res);
            }
        }
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < arr.get(i).len(); ++j) {
                int res = 0;
                for (int k = 0; k <= i; ++k) {
                    if (prefSum.get(k).len() != 0) {
                        res += prefSum.get(k).get(Math.min(j, prefSum.get(k).len() - 1));
                    }
                }
                System.out.print(res);
                System.out.print(' ');
            }
            System.out.print('\n');
        }
    }
}