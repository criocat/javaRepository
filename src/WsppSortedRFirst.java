import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.Math;
import java.util.*;

public class WsppSortedRFirst {

    static class IntList {
        private int len = 0;
        private int[] arr = new int[1];

        public IntList() {
        }

        public IntList(int val) {
            add(val);
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

    private static boolean good(char c) {
        return Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.print("not valid number of arguments");
            return;
        }
        Map<String, IntList> stringMap = new TreeMap<>();
        Map<String, Integer> countHashTable = new HashMap<>();
        try {
            MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
            String curLine = in.nextLine();
            int countWord = 0;
            while (curLine != null) {
                Set<String> curSet = new HashSet<>();
                for (int i = 0; i < curLine.length(); ++i) {
                    if (good(curLine.charAt(i))) {
                        int poiterR = i;
                        while (poiterR < curLine.length() && good(curLine.charAt(poiterR))) {
                            ++poiterR;
                        }
                        String curWord = curLine.substring(i, poiterR).toLowerCase();
                        curWord = new StringBuilder(curWord).reverse().toString();
                        ++countWord;
                        i = poiterR - 1;
                        if (!curSet.contains(curWord)) {
                            curSet.add(curWord);
                            IntList curIntList = stringMap.get(curWord);
                            if (curIntList == null) {
                                stringMap.put(curWord, new IntList(countWord));
                            } else {
                                curIntList.add(countWord);
                            }
                        }
                        Integer countCurWord = countHashTable.get(curWord);
                        countHashTable.put(curWord, (countCurWord == null ? 0 : countCurWord) + 1);
                    }
                }
                curLine = in.nextLine();
            }
            in.close();
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            try {
                for (Map.Entry<String, IntList> cur : stringMap.entrySet()) {
                    IntList curIntList = cur.getValue();
                    out.write(new StringBuilder(cur.getKey()).reverse() + " " + countHashTable.get(cur.getKey()));
                    for (int j = 0; j < curIntList.len(); ++j) {
                        out.write(" " + curIntList.get(j));
                    }
                    out.write('\n');
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            System.out.println("an error occurred while writing: " + e.getMessage());
        }
    }
}
