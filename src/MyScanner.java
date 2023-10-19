import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.lang.Math;

public class MyScanner {
    private Reader rd;
    private final int buffSize = 1024;
    private final String lineSeparator = System.lineSeparator();
    private int pos = buffSize;
    private final char nullChar = (char) -1;
    private char[] buffer = new char[buffSize];

    public MyScanner(InputStream in) {
        rd = new InputStreamReader(in);
    }

    public MyScanner(String path, Charset charset) {
        try {
            rd = new FileReader(path, charset);
        } catch (IOException e) {
            System.out.print("the error occurred when opening the file " + e.getMessage());
        }
    }

    private boolean good(char c, boolean readDigits) {
        boolean res = Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
        return (readDigits ? res || Character.isDigit(c) : res);
    }

    private void updateBuffer() {
        try {
            int readedLen = 0;
            while (readedLen != buffSize) {
                int cnt = rd.read(buffer, readedLen, buffSize - readedLen);
                if (cnt == -1) {
                    buffer[readedLen] = nullChar;
                    break;
                }
                readedLen += cnt;
            }
        } catch (IOException e) {
            System.out.print("reading error " + e.getMessage());
        }
    }

    public String next(boolean readDigits) {
        while (true) {
            if (pos == buffSize) {
                updateBuffer();
                pos = 0;
            }
            if (buffer[pos] == nullChar || good(buffer[pos], readDigits)) {
                break;
            }
            ++pos;
        }
        if (pos != buffSize && buffer[pos] == nullChar) {
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
                if (!good(buffer[i], readDigits)) {
                    --pointerR;
                    break;
                }
                res.append(buffer[i]);
            }
            pos = pointerR + 1;
        }
        return res.toString();
    }

    public String nextLine() {
        StringBuilder res = new StringBuilder();
        while (true) {
            if (pos == buffSize) {
                updateBuffer();
                pos = 0;
            }
            if (buffer[pos] == nullChar) {
                if (res.length() == 0) {
                    return null;
                }
                return res.toString();
            }
            res.append(buffer[pos]);
            ++pos;
            if (res.length() >= lineSeparator.length() && res.substring(res.length() - lineSeparator.length()).equals(lineSeparator)) {
                return res.substring(0, res.length() - lineSeparator.length());
            }
        }
    }

    public boolean hasLineSeparator(boolean readDigits) {
        while (true) {
            if (pos == buffSize) {
                updateBuffer();
                pos = 0;
            }

        }
    }

    public void close() {
        try {
            rd.close();
        } catch (IOException e) {
            System.out.print("error when trying to close the file " + e.getMessage());
        }
    }
}