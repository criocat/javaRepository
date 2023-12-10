import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatCountAffixL_v1 {

    public static boolean good(char c) {
        return Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    static char[] push(char[] arr, int len, char val) {
        if (len == arr.length) {
            arr = Arrays.copyOf(arr, arr.length * 2);
        }
        arr[len] = val;
        return arr;
    }

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
        Map<String, Integer> hashTable = new HashMap<>();
        char[] charArr = new char[1];
        int charArrLen = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
            try {
                int c = in.read();
                while (c != -1) {
                    charArr = push(charArr, charArrLen, (char) c);
                    charArrLen++;
                    c = in.read();
                }
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file to read in directory: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("an error occurred while reading: " + e.getMessage());
        }
        String[] allWords = new String[1];
        int allWordsLen = 0;
        for (int i = 0; i < charArrLen; ++i) {
            if (good(charArr[i])) {
                int pointerR = i;
                while (pointerR < charArrLen && good(charArr[pointerR])) {
                    ++pointerR;
                }
                String curWord = new String(charArr, i, pointerR - i).toLowerCase();
                i = pointerR - 1;
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
            }
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            Integer[][] doubleArray = new Integer[1][];
            int doubleArrayLen = 0;
            for (int i = 0; i < allWordsLen; ++i) {
                doubleArray = push(doubleArray, doubleArrayLen, new Integer[]{hashTable.get(allWords[i]), i});
                doubleArrayLen++;
            }
            doubleArray = Arrays.copyOf(doubleArray, doubleArrayLen);
            Arrays.sort(doubleArray, new Comparator<Integer[]>() {
                public int compare(Integer[] arr1, Integer[] arr2) {
                    if (Objects.equals(arr1[0], arr2[0])) {
                        return (arr1[1] < arr2[1] ? -1 : 1);
                    }
                    return (arr1[0] < arr2[0] ? -1 : 1);
                }
            });
            for (int i = 0; i < doubleArrayLen; ++i) {
                out.write(allWords[doubleArray[i][1]] + " " + doubleArray[i][0] + '\n');
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