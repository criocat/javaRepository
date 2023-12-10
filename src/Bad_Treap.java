import java.io.*;
import java.nio.charset.Charset;

public class Bad_Treap {

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

    public static void main(String[] args) {
        long n;
        MyScanner sc = new MyScanner(System.in);
        try {
            n = sc.nextLong();
            long cnt = -710 * 25000;
            for (int i = 0; i < n; ++i) {
                System.out.println(cnt);
                cnt += 710;
            }
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
    }
}
