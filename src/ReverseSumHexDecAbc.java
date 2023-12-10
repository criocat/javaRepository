import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;

public class ReverseSumHexDecAbc {

    static class IntArray {
        private int maxLen = 1;
        private int len = 0;
        private int[] arr = new int[1];

        private void expand() {
            maxLen *= 2;
            arr = Arrays.copyOf(arr, maxLen);
        }

        public void add(int num) {
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

    static class IntArray2D {
        private int maxLen = 1;
        private int len = 0;
        private IntArray[] arr = new IntArray[1];

        private void expand() {
            maxLen *= 2;
            arr = Arrays.copyOf(arr, maxLen);
        }

        public void add(IntArray curArr) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = curArr;
        }

        public IntArray get(int ind) {
            return arr[ind];
        }
    }

    private static int getDecimalNumber(String str) {
        int index = (str.charAt(0) == '-' ? 1 : 0);
        if (index == 1 && str.length() == 1) {
            throw new NumberFormatException();
        }
        if (str.length() >= 3 && str.charAt(0) == '0' && Character.toLowerCase(str.charAt(1)) == 'x') {
            return Integer.parseUnsignedInt(str.substring(2), 16);
        } else if ('0' <= str.charAt(index) && str.charAt(index) <= '9') {
            return Integer.parseInt(str);
        } else {
            char[] chars = new char[str.length()];
            if (index == 1) {
                chars[0] = '-';
            }
            for (int i = index; i < str.length(); ++i) {
                chars[i] = (char) (str.charAt(i) - 'a' + '0');
            }
            return Integer.parseInt(new String(chars));
        }
    }

    private static String getAbcNumber(int num) {
        String str = "" + num;
        int index = (str.charAt(0) == '-' ? 1 : 0);
        char[] chars = new char[str.length()];
        for (int i = index; i < str.length(); i++) {
            chars[i] = (char) (str.charAt(i) + 'a' - '0');
        }
        if (index == 1) {
            chars[0] = '-';
        }
        return new String(chars);
    }

    public static boolean validCharacter(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c) || c == '-';
    }

    public static void main(String[] args) {
        IntArray2D arr = new IntArray2D();
        int lineCount = 0;
        MyScanner sc = new MyScanner(System.in);
        ScannerFilter isValidCharacter = (char c) -> (Character.isAlphabetic(c) || Character.getType(c) == Character.DASH_PUNCTUATION  || Character.isDigit(c));
        try {
            String curString = "";
            while (curString != null) {
                arr.add(new IntArray());
                ++lineCount;
                while (!sc.hasNextLine(isValidCharacter)) {
                    curString = sc.next(isValidCharacter, Integer.MAX_VALUE);
                    if (curString == null) {
                        break;
                    }
                    arr.get(lineCount - 1).add(getDecimalNumber(curString));
                }
            }
            sc.close();
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
        int maxRowLen = -1;
        for (int i = 0; i < lineCount; ++i) {
            maxRowLen = Math.max(maxRowLen, arr.get(i).len());
        }
        int[] prefSumCol = new int[maxRowLen];
        for (int i = 0; i < lineCount - 1; ++i) {
            for (int j = 0; j < arr.get(i).len(); ++j) {
                prefSumCol[j] += arr.get(i).get(j);
                arr.get(i).set(j, (j == 0 ? 0 : arr.get(i).get(j - 1)) + prefSumCol[j]);
                System.out.print(getAbcNumber(arr.get(i).get(j)));
                System.out.print(' ');
            }
            System.out.print('\n');
        }
    }
}
