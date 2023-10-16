import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.lang.Math;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class MyScanner {
    private Reader rd;
    private final int buffSize = 1024;
    private String str = "";
    private int pos = buffSize;
    private final char nullChar = (char)(-1);
    private char[] buffer = new char[buffSize];
    public MyScanner(InputStream in) {
        rd = new InputStreamReader(in);
        str = new String(buffer);
    }

    public MyScanner(String path, Charset charset) {
        try {
            rd = new FileReader(path, charset);
            str = new String(buffer);
        } catch(IOException e) {
            System.out.print("the error occurred when opening the file " + e.getMessage());
        }
    }

    public MyScanner(String string) {
        rd = new StringReader(string);
        str = new String(buffer);
    }

    public boolean good(char c) {
        return Character.isAlphabetic(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION;
    }

    private void updateBuffer() {
        try {
            int cnt = Math.max(rd.read(buffer, 0, buffSize), 0);
            if (cnt != buffSize) {
                buffer[cnt] = nullChar;
            }
            str = new String(buffer);
        } catch (IOException e) {
            System.out.print("reading error " + e.getMessage());
        }
    }
    public String next() {
        while (pos < str.length() && str.charAt(pos) != nullChar && !good(str.charAt(pos))) {
            ++pos;
            if (pos == str.length()) {
                updateBuffer();
                pos = 0;
            }
        }
        if (pos != str.length() && buffer[pos] == nullChar) {
            return null;
        }
        int pointerR;
        StringBuilder res = new StringBuilder();
        boolean first = true;
        while (pos == str.length() || first) {
            first = false;
            if (pos == str.length()) {
                pos = 0;
                updateBuffer();
            }
            pointerR = pos;
            for (int i = pos; i < str.length(); ++i) {
                pointerR = i;
                if (!good(str.charAt(i))) {
                    --pointerR;
                    break;
                }
                res.append(str.charAt(i));
            }
            pos = pointerR + 1;
        }
        return res.toString();
    }

    public String nextLine() {
        StringBuilder res = new StringBuilder();
        while (true) {
            if (pos == str.length()) {
                updateBuffer();
                pos = 0;
            }
            if (buffer[pos] == nullChar) {
                if (res.length() == 0) {
                    return null;
                }
                return res.toString();
            }
            if (str.charAt(pos) != '\n') {
                if (str.charAt(pos) != '\r') {
                    res.append(str.charAt(pos));
                }
            }
            else {
                ++pos;
                return res.toString();
            }
            ++pos;
        }
    }

    public char read() {
        if (pos == str.length()) {
            updateBuffer();
            pos = 0;
        }
        if (str.charAt(pos) == nullChar) {
            return nullChar;
        }
        return(str.charAt(pos++));
    }

    public void close() {
        try {
        rd.close();
        } catch(IOException e) {
            System.out.print("error when trying to close the file " + e.getMessage());
        }
    }
    public char getNullChar() {
        return nullChar;
    }
}