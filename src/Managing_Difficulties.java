import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Managing_Difficulties {

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
            String num = next(Character::isDigit, Integer.MAX_VALUE);
            return Long.parseLong(num);
        }

        public int nextInt() throws IOException {
            String num = next(Character::isDigit, Integer.MAX_VALUE);
            return Integer.parseInt(num);
        }

        public String nextLine() throws IOException {
            return next((char c) -> (c != '\n' && c != '\r' && c != '\u000C' && c != '\u000B' &&
                    c != '\uu0085' && c != '\u2028' && c != '\u2029'), System.lineSeparator().length());
        }

        public boolean hasNextLine(ScannerFilter separators) throws IOException {
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

    public static void main(String[] args) {
        long t;
        Scanner sc = new Scanner(System.in);
        t = sc.nextLong();
        for (int test = 0; test < t; ++test) {
            long n = sc.nextLong();
            long ans = 0;
            ArrayList<Integer> nums = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                nums.add(sc.nextInt());
            }
            HashMap<Integer, Integer> hashMap = new HashMap<>();

            for (int j = 0; j < n; ++j) {

                for (int k = j + 1; k < n; ++k) {
                    int ai = 2 * nums.get(j) - nums.get(k);
                    Integer cnt = hashMap.get(ai);
                    if (cnt == null) {
                        cnt = 0;
                    }
                    ans += cnt;
                }
                Integer cnt = hashMap.get(nums.get(j));
                if (cnt == null) {
                    cnt = 0;
                }
                hashMap.put(nums.get(j), cnt + 1);
            }
            System.out.println(ans);
        }
        sc.close();

    }
}
