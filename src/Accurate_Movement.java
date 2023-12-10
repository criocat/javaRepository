import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Accurate_Movement {

    public interface ScannerFilter {
        public boolean isValidCharacter(char c);
    }

    public static class MyScanner {
        private Reader rd;
        private final int buffSize = 2048;
        private int pos = buffSize;
        private final char nullChar = (char) -1;
        private char[] buffer = new char[buffSize];

        public MyScanner(InputStream in) {
            rd = new InputStreamReader(in);
        }

        public MyScanner(String path, Charset charset) throws IOException {
            rd = new FileReader(path, charset);
        }

        private void updateBuffer() throws IOException {
            int readedLen = 0;
            while (readedLen != buffSize) {
                int cnt = rd.read(buffer, readedLen, buffSize - readedLen);
                if (cnt == -1) {
                    buffer[readedLen] = nullChar;
                    break;
                }
                readedLen += cnt;
            }
        }

        public String next(ScannerFilter separators, int maxSeparatorLen) throws IOException {
            int cnt = 0;
            while (true) {
                if (pos == buffSize) {
                    if (buffer[pos - 1] == 0) {
                        ++cnt;
                    }
                    updateBuffer();
                    pos = 0;
                }
                if (buffer[pos] == nullChar || separators.isValidCharacter(buffer[pos])) {
                    break;
                }
                if (cnt == maxSeparatorLen) {
                    return "";
                }
                ++pos;
                ++cnt;
            }
            if (buffer[pos] == nullChar) {
                return null;
            }
            int pointerR;
            StringBuilder res = new StringBuilder();
            boolean first = true;
            while (pos == buffSize || first) {
                first = false;
                if (pos == buffSize) {
                    pos = 0;
                    updateBuffer();
                }
                pointerR = pos;
                for (int i = pos; i < buffSize; ++i) {
                    pointerR = i;
                    if (!separators.isValidCharacter(buffer[i]) || buffer[i] == nullChar) {
                        --pointerR;
                        break;
                    }
                    res.append(buffer[i]);
                }
                pos = pointerR + 1;
            }
            return res.toString();
        }
        public long nextLong() throws IOException {
            String num = next((char c) -> (Character.isDigit(c) || c == '-'), Integer.MAX_VALUE);
            return Long.parseLong(num);
        }

        public int nextInt() throws IOException {
            String num = next((char c) -> (Character.isDigit(c) || c == '-'), Integer.MAX_VALUE);
            return Integer.parseInt(num);
        }

        public String nextLine() throws IOException {
            return next((char c) -> (c != '\n' && c != '\r' && c != '\u000C' && c != '\u000B' &&
                    c != '\uu0085' && c != '\u2028' && c !='\u2029'), System.lineSeparator().length());
        }

        public boolean hasNextLine(ScannerFilter separators) throws  IOException {
            StringBuilder tail = new StringBuilder();
            while (true) {
                if (pos == buffSize) {
                    pos = 0;
                    updateBuffer();
                }
                if (buffer[pos] == nullChar || separators.isValidCharacter(buffer[pos])) {
                    return false;
                }
                tail.append(buffer[pos]);
                ++pos;
                if (tail.length() >= System.lineSeparator().length() && tail.substring(tail.length() - System.lineSeparator().length()).equals(System.lineSeparator())) {
                    return true;
                }
            }
        }
        public void close() throws IOException {
            rd.close();
        }
    }

    static int mod = 998244353;

    static ArrayList<Integer> getDel(int x) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 1; i * i <= x; ++i) {
            if (x % i == 0) {
                ans.add(i);
                if (i * i != x) {
                    ans.add(x / i);
                }
            }
        }
        ans.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 < o2) {
                    return -1;
                } else if (o1 > o2) {
                    return 1;
                }
                return 0;
            }
        });
        return ans;
    }

    public static void main(String[] args) {
        int n, k;
        MyScanner sc = new MyScanner(System.in);
        try {
            n = sc.nextInt();
            k = sc.nextInt();
            ArrayList<ArrayList<Integer>> R = new ArrayList<>();
            for (int i = 0; i < k + 1; ++i) {
                R.add(new ArrayList<Integer>());
                R.get(i).add(1);
            }
            for (int i = 1; i <= k; ++i) {
                for (int j = 1; j <= n; ++j) {
                    R.get(i).add((R.get(i).get((j == 1 ? 0 : j - 2)) * i) % mod);
                }
            }
            ArrayList<Integer> D = new ArrayList<>();
            D.add(1);
            D.add(k);
            for (int i = 2; i <= n; ++i) {
                long sum = 0;
                ArrayList<Integer> del = getDel(i);
                for (int pos = 0; pos < del.size() - 1; ++pos) {
                    int l = del.get(pos);
                    sum += ((long)n / l) * D.get(l);
                    sum %= mod;
                }
                D.add((R.get(i).get(k) - (int)sum + mod) % mod);
            }
            for (int i = 1; i <= n; ++i) {
                System.out.print(D.get(i) + " ");
            }
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
    }
}