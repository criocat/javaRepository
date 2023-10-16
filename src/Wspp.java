import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.Math;
import java.util.*;

public class Wspp {

    static class IntList {
        private int len = 0;
        private int[] arr = new int[1];

        public IntList() {}

        public void push_back(int num) {
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
        MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
        String curWord = in.next();
        int cnt = 0;
        while (curWord != null) {
            ++cnt;
            curWord = curWord.toLowerCase();
            IntList curArray = hashTable.get(curWord);
            if (curArray == null) {
                IntList curIntList = new IntList();
                curIntList.push_back(cnt);
                hashTable.put(curWord, curIntList);
            } else {
                curArray.push_back(cnt);
            }
            curWord = in.next();
        }
        in.close();
        ArrayList<Map.Entry<String, IntList>> arr = new ArrayList<>();
        for (Map.Entry<String, IntList> pair : hashTable.entrySet()) {
            arr.add(pair);
        }
        arr.sort(new Comparator<Map.Entry<String, IntList>>() {
            @Override
            public int compare(Map.Entry<String, IntList> pair1, Map.Entry<String, IntList> pair2) {
                return (pair1.getValue().get(0) < pair2.getValue().get(0) ? -1 : 1);
            }
        });
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            for (int i = 0; i < arr.size(); ++i) {
                out.write(arr.get(i).getKey() + " " + arr.get(i).getValue().len());
                for (int j = 0; j < arr.get(i).getValue().len(); ++j) {
                    out.write(" " + arr.get(i).getValue().get(j));
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
