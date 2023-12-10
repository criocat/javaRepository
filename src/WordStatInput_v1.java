import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;

public class WordStatInput_v1 {

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
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8));
            try {
                char[] buffer = new char[1024];
                int read = in.read(buffer, 0, buffer.length);
                while (read != -1) {
                    for (int i = 0; i < read; ++i) {
                        charArr.push_back(buffer[i]);
                    }
                    read = in.read(buffer, 0, buffer.length);
                }
            } catch (IOException e) {
                System.out.println("an error occurred while reading: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file to read in directory: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("an error occurred when trying to close the reading stream: " + e.getMessage());
                }
            }
        }
        StringArray allWords = new StringArray();
        int len = charArr.len();
        for (int i = 0; i < len; ++i) {
            if (good(charArr.get(i))) {
                int pointerR = -1;
                for (int j = i; j < len && good(charArr.get(j)); ++j) {
                    pointerR = j;
                }
                String curWord = new String(charArr.arr, i, pointerR - i + 1).toLowerCase();
                i = pointerR;
                int cnt = hashTable.add(curWord, allWords.len());
                if (cnt == 1) allWords.push_back(curWord);
            }
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
