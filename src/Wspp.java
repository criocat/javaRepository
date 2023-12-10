import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.Math;
import java.util.*;

public class Wspp {

    static class IntList {
        private int len = 0;
        private int[] arr = new int[1];

        public IntList() {
        }

        public void add(int num) {
            if (len == arr.length) {
                arr = Arrays.copyOf(arr, 2 * arr.length);
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

    public static void main(String[] args) {
        Map<String, IntList> hashTable = new HashMap();
        ArrayList<String> allWords = new ArrayList<>();
        int cnt = 0;
        try {
            MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
            ScannerFilter isValidCharacter = (char c) -> (Character.isAlphabetic(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'');
            String curWord = in.next(isValidCharacter, Integer.MAX_VALUE);
            while (curWord != null) {
                ++cnt;
                curWord = curWord.toLowerCase();
                IntList curArray = hashTable.get(curWord);
                if (curArray == null) {
                    allWords.add(curWord);
                    IntList curIntList = new IntList();
                    curIntList.add(cnt);
                    hashTable.put(curWord, curIntList);
                } else {
                    curArray.add(cnt);
                }
                curWord = in.next(isValidCharacter, Integer.MAX_VALUE);
            }
            in.close();
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            for (int i = 0; i < allWords.size(); ++i) {
                IntList curIntList = hashTable.get(allWords.get(i));
                out.write(allWords.get(i) + " " + curIntList.len());
                for (int j = 0; j < curIntList.len(); ++j) {
                    out.write(" " + curIntList.get(j));
                }
                out.write('\n');
            }
        } catch (IOException e) {
            System.out.println("an error occurred while writing: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("an error occurred when trying to close the writing stream: " + e.getMessage());
                }
            }
        }
    }
}
