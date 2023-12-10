//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.NoSuchFileException;
//import java.util.Arrays;
//import java.util.HashMap;
//public class WordStatInput_v2 {
//
//    static class Node {
//        String str;
//        int cnt = 0, pos = -1;
//        public Node() {
//        }
//        public Node(String string, int position, int count) {
//            cnt = count;
//            str = string;
//            pos = position;
//        }
//    }
//
//    static class NodeArray {
//        private int len = 0;
//        private Node[] arr = new Node[1];
//        private void expand() {
//            arr = Arrays.copyOf(arr, 2 * arr.length);
//        }
//        public void push_back(Node nd) {
//            if (len == arr.length) {
//                expand();
//            }
//            arr[len++] = nd;
//        }
//        public Node get(int ind) {
//            return arr[ind];
//        }
//        public int len() {
//            return len;
//        }
//    }
//
//    static class HashTable {
//        private int maxLen = 1;
//        private int len = 0;
//        private int p = 157;
//        private NodeArray[] hashTable = new NodeArray[]{new NodeArray()};
//        public HashTable() {}
//        private int getHash(String str) {
//            long hash = 0;
//            for (int i = 0; i < str.length(); ++i) {
//                hash = (hash * p + (long) str.charAt(i)) % maxLen;
//            }
//            return (int) hash;
//        }
//        private void expand() {
//            maxLen *= 2;
//            NodeArray[] newArr = new NodeArray[maxLen];
//            for (int i = 0; i < maxLen; ++i) {
//                newArr[i] = new NodeArray();
//            }
//            for (int i = 0; i < hashTable.length; ++i) {
//                for (int j = 0; j < hashTable[i].len(); ++j) {
//                    Node nd = hashTable[i].get(j);
//                    int hash = getHash(nd.str);
//                    newArr[hash].push_back(new Node(nd.str, nd.pos, nd.cnt));
//                }
//            }
//            hashTable = newArr;
//        }
//        public int add(String str, int difLen) {
//            if (len == maxLen) {
//                expand();
//            }
//            int hash = getHash(str);
//            for (int i = 0; i < hashTable[hash].len(); ++i) {
//                if (hashTable[hash].get(i).str.equals(str)) {
//                    hashTable[hash].get(i).cnt++;
//                    len++;
//                    return hashTable[hash].get(i).cnt;
//                }
//            }
//            hashTable[hash].push_back(new Node(str, difLen, 1));
//            return 1;
//        }
//        public int getCnt(String str) {
//            int hash = getHash(str);
//            for (int i = 0; i < hashTable[hash].len(); ++i) {
//                if (hashTable[hash].get(i).str.equals(str)) {
//                    return hashTable[hash].get(i).cnt;
//                }
//            }
//            return 0;
//        }
//    }
//
//    public static boolean good(char c) {
//        return Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
//    }
//
//    static char[] push(char[] arr, int len, char val) {
//        if (len == arr.length) {
//            arr = Arrays.copyOf(arr, arr.length * 2);
//        }
//        arr[len++] = val;
//        return arr;
//    }
//
//    static String[] push(String[] arr, int len, String val) {
//        if (len == arr.length) {
//            arr = Arrays.copyOf(arr, arr.length * 2);
//        }
//        arr[len++] = val;
//        return arr;
//    }
//    static int[][] push(int[][] arr, int len, int[] val) {
//        if (len == arr.length) {
//            arr = Arrays.copyOf(arr, arr.length * 2);
//        }
//        arr[len++] = val;
//        return arr;
//    }
//
//    static boolean comp(int[] a, int[] b) {
//        if (a[0] == b[0]) {
//            return a[1] < b[1];
//        }
//        return a[0] < b[0];
//    }
//
//    static int[][] merge(int[][] a, int[][] b) {
//        int[][] res = new int[a.length + b.length][];
//        int pa = 0, pb = 0;
//        for (int i = 0; i < a.length + b.length; ++i) {
//            if (pb == b.length || (pa != a.length && comp(a[pa], b[pb]))) {
//                res[i] = a[pa++];
//            } else {
//                res[i] = b[pb++];
//            }
//        }
//        return res;
//    }
//
//    static int[][] sort(int[][] arr) {
//        if (arr.length == 1) {
//            return arr;
//        }
//        int mid = arr.length / 2;
//        int[][] left = new int[mid][];
//        int[][] right = new int[arr.length - mid][];
//        for (int i = 0; i < mid; ++i) {
//            left[i] = arr[i];
//        }
//        for (int i = mid; i < arr.length; ++i) {
//            right[i - mid] = arr[i];
//        }
//        return merge(sort(left), sort(right));
//    }
//    public static void main(String[] args) {
//        HashMap<String, Integer> hashTable = new HashMap<String, Integer>();
//        char[] charArr = new char[1];
//        int charArrLen = 0;
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new InputStreamReader(new FileInputStream("input.txt"), StandardCharsets.UTF_8));
//            try {
//                char[] buffer = new char[1024];
//                int read = in.read(buffer, 0, buffer.length);
//                while (read != -1) {
//                    for (int i = 0; i < read; ++i) {
//                        charArr = push(charArr, charArrLen, buffer[i]);
//                        charArrLen++;
//                    }
//                    read = in.read(buffer, 0, buffer.length);
//                }
//            } catch (IOException e) {
//                System.out.println("an error occurred while reading: " + e.getMessage());
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("No file to read in directory: " + e.getMessage());
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    System.out.println("an error occurred when trying to close the reading stream: " + e.getMessage());
//                }
//            }
//        }
//        String[] allWords = new String[1];
//        int allWordsLen = 0;
//        for (int i = 0; i < charArrLen; ++i) {
//            if (good(charArr[i])) {
//                int pointerR = -1;
//                for (int j = i; j < charArrLen && good(charArr[j]); ++j) {
//                    pointerR = j;
//                }
//                String curWord = new String(charArr, i, pointerR - i + 1).toLowerCase();
//                i = pointerR;
//                if (curWord.length() >= 2) {
//                    String curWordl = curWord.substring(0, 2);
//                    int cnt = hashTable.get(curWordl);
//                    if (cnt == null) {
//                        cnt = 0;
//                    }
//                    hashTable.put(curWordl, cnt+ 1);
//                    if (cnt == 0) {
//                        allWords = push(allWords, allWordsLen, curWordl);
//                        allWordsLen++;
//                    }
//                    String curWordr = curWord.substring(curWord.length() - 2, curWord.length());
//                    cnt = hashTable.get(curWordr);
//                    hashTable.put(curWordl, cnt+ 1);
//                    if (cnt == 0) {
//                        allWords = push(allWords, allWordsLen, curWordr);
//                        allWordsLen++;
//                    }
//                }
//            }
//        }
//
//        BufferedWriter out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), StandardCharsets.UTF_8));
//            char[] writeChar = new char[1];
//            int writeCharLen = 0;
//            int[][] doublearr = new int[1][];
//            int doublearrLen = 0;
//            for (int i = 0; i < allWordsLen; ++i) {
//                doublearr = push(doublearr, doublearrLen, new int[]{hashTable.get(allWords[i]), i});
//                doublearrLen++;
//            }
//            doublearr = Arrays.copyOf(doublearr, doublearrLen);
//            doublearr = sort(doublearr);
//            for (int i = 0; i < allWordsLen; ++i) {
//                String tempStr = allWords[doublearr[i][1]] + " " + doublearr[i][0] + "\n";
//                for (int j = 0; j < tempStr.length(); ++j) {
//                    writeChar = push(writeChar, writeCharLen, tempStr.charAt(j));
//                    writeCharLen++;
//                }
//            }
//            out.write(writeChar, 0, writeCharLen);
//        } catch (IOException e) {
//            System.out.println("an error occurred while writing: " + e.getMessage());
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    System.out.println("an error occurred when trying to close the writing stream: " + e.getMessage());
//                }
//            }
//        }
//    }
//}
