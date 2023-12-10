import java.io.*;
import java.nio.charset.StandardCharsets;

public class WordStatInput {

    static class CharArray {
        int maxLen = 1;
        int len = 0;
        char[] arr = new char[1];

        private void expand() {
            maxLen *= 2;
            char[] newArr = new char[maxLen];
            for (int i = 0; i < arr.length; ++i) {
                newArr[i] = arr[i];
            }
            arr = newArr;
        }

        public void push_back(char c) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = c;
        }

        public char get(int ind) {
            return arr[ind];
        }

        public int len() {
            return len;
        }

        public void push_string(String str) {
            for (int i = 0; i < str.length(); ++i) {
                push_back(str.charAt(i));
            }
        }
    }

    static class StringArray {
        int maxLen = 1;
        int len = 0;
        String[] arr = new String[1];

        private void expand() {
            maxLen *= 2;
            String[] newArr = new String[maxLen];
            for (int i = 0; i < arr.length; ++i) {
                newArr[i] = arr[i];
            }
            arr = newArr;
        }

        public void push_back(String str) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = str;
        }

        public String get(int ind) {
            return arr[ind];
        }

        public int len() {
            return len;
        }
    }

    static class Node {
        String str;
        int cnt = 0, pos = -1;

        public Node() {
        }

        public Node(String string, int position, int count) {
            cnt = count;
            str = string;
            pos = position;
        }
    }

    static class NodeArray {
        int maxLen = 1;
        int len = 0;
        Node[] arr = new Node[1];

        private void expand() {
            maxLen *= 2;
            Node[] newArr = new Node[maxLen];
            for (int i = 0; i < arr.length; ++i) {
                newArr[i] = arr[i];
            }
            arr = newArr;
        }

        public void push_back(Node nd) {
            if (len == maxLen) {
                expand();
            }
            arr[len++] = nd;
        }

        public Node get(int ind) {
            return arr[ind];
        }

        public int len() {
            return len;
        }
    }

    static class HashTable {
        int maxLen = 1;
        int len = 0;
        int p = 157;
        NodeArray[] hashTable = new NodeArray[]{new NodeArray()};

        public HashTable() {
        }

        private int getHash(String str) {
            long hash = 0;
            for (int i = 0; i < str.length(); ++i) {
                hash = (hash * p + (long) str.charAt(i)) % maxLen;
            }
            return (int) hash;
        }

        private void expand() {
            maxLen *= 2;
            NodeArray[] newArr = new NodeArray[maxLen];
            for (int i = 0; i < maxLen; ++i) {
                newArr[i] = new NodeArray();
            }
            for (int i = 0; i < hashTable.length; ++i) {
                for (int j = 0; j < hashTable[i].len(); ++j) {
                    Node nd = hashTable[i].get(j);
                    int hash = getHash(nd.str);
                    newArr[hash].push_back(new Node(nd.str, nd.pos, nd.cnt));
                }
            }
            hashTable = newArr;
        }

        public int add(String str, int difLen) {
            if (len == maxLen) {
                expand();
            }
            int hash = getHash(str);
            for (int i = 0; i < hashTable[hash].len(); ++i) {
                if (hashTable[hash].get(i).str.equals(str)) {
                    hashTable[hash].get(i).cnt++;
                    len++;
                    return hashTable[hash].get(i).cnt;
                }
            }
            hashTable[hash].push_back(new Node(str, difLen, 1));
            return 1;
        }

        public int getCnt(String str) {
            int hash = getHash(str);
            for (int i = 0; i < hashTable[hash].len(); ++i) {
                if (hashTable[hash].get(i).str.equals(str)) {
                    return hashTable[hash].get(i).cnt;
                }
            }
            return 0;
        }
    }

    public static boolean good(char c) {
        return Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    public static void main(String[] args) {
        HashTable hashTable = new HashTable();
        CharArray charArr = new CharArray();
        StringArray allWords = new StringArray();
        int len = charArr.len();
        ScannerFilter isValidCharacter = (char c) -> (Character.isAlphabetic(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'');
        try {
            FileWriter fil = new FileWriter("output.txt");
            MyScanner in = new MyScanner(args[0], StandardCharsets.UTF_8);
            String curWord = in.next(isValidCharacter, Integer.MAX_VALUE);
            while (curWord != null) {
                fil.write(curWord);
                fil.write('\n');
                curWord = curWord.toLowerCase();
                int cnt = hashTable.add(curWord, allWords.len());
                if (cnt == 1) allWords.push_back(curWord);
                curWord = in.next(isValidCharacter, Integer.MAX_VALUE);
            }
            in.close();
            fil.close();
        } catch (IOException e) {
            System.out.print("error" + e.getMessage());
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            CharArray writeChar = new CharArray();
            for (int i = 0; i < allWords.len(); ++i) {
                writeChar.push_string(allWords.get(i) + " " + hashTable.getCnt(allWords.get(i)) + "\n");
            }
            out.write(writeChar.arr, 0, writeChar.len());
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
