import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WordStatCountAffixL {

    static String[] push(String[] arr, int len, String val) {
        if (len == arr.length) {
            arr = Arrays.copyOf(arr, arr.length * 2);
        }
        arr[len] = val;
        return arr;
    }

    static Integer[][] push(Integer[][] arr, int len, Integer[] val) {
        if (len == arr.length) {
            arr = Arrays.copyOf(arr, arr.length * 2);
        }
        arr[len] = val;
        return arr;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.print("Not valid number of arguments");
            return;
        }
        Map<String, Integer> hashTable = new HashMap<>();
        String[] allWords = new String[1];
        int allWordsLen = 0;
        try {
            MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
            ScannerFilter isValidCharacter = (char c) -> (Character.isAlphabetic(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'');
            String curWord = in.next(isValidCharacter, Integer.MAX_VALUE);
            while (curWord != null) {
                curWord = curWord.toLowerCase();
                if (curWord.length() >= 2) {
                    String[] strings = {curWord.substring(0, 2), curWord.substring(curWord.length() - 2)};
                    for (String curString : strings) {
                        Integer cnt = hashTable.get(curString);
                        if (cnt == null || cnt == 0) {
                            allWords = push(allWords, allWordsLen, curString);
                            allWordsLen++;
                        }
                        hashTable.put(curString, (cnt == null ? 1 : cnt + 1));
                    }
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
            try {
                Integer[][] doubleArray = new Integer[1][];
                int doubleArrayLen = 0;
                for (int i = 0; i < allWordsLen; ++i) {
                    doubleArray = push(doubleArray, doubleArrayLen, new Integer[]{hashTable.get(allWords[i]), i});
                    doubleArrayLen++;
                }
                doubleArray = Arrays.copyOf(doubleArray, doubleArrayLen);
                Arrays.sort(doubleArray, new Comparator<Integer[]>() {
                    public int compare(Integer[] arr1, Integer[] arr2) {
                        if (arr1[0] == arr2[0]) {
                            return (arr1[1] < arr2[1] ? -1 : 1);
                        }
                        return (arr1[0] < arr2[0] ? -1 : 1);
                    }
                });
                for (int i = 0; i < doubleArrayLen; ++i) {
                    out.write(allWords[doubleArray[i][1]] + " " + doubleArray[i][0] + '\n');
                }
            } finally {
                out.close();
            }
        } catch (IOException e) {
            System.out.println("an error occurred while writing: " + e.getMessage());
        }
    }
}