package md2html_v2;

import java_classes.MyScanner;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Md2Html_v2 {

    static int[] push(int[] arr, int arrLen, int val) {
        if (arr.length == arrLen) {
            arr = Arrays.copyOf(arr, 2 * arrLen);
        }
        arr[arrLen] = val;
        return arr;
    }

    final static String[] markup = new String[]{"<em>", "</em>", "<strong>", "</strong>", "<code>", "</code>", "<s>", "</s>", "<pre>", "</pre>", "", "&lt;", "&gt;", "&amp;"};
    static int[] type, pos, markupIndex;

    static void pushToken(int curType, int curPos, int curMarkupIndex, int typeLen) {
        type = push(type, typeLen, curType);
        pos = push(pos, typeLen, curPos);
        markupIndex = push(markupIndex, typeLen, curMarkupIndex);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.print("not valid number of arguments");
            return;
        }
        try {
            MyScanner sc = new MyScanner(args[0], StandardCharsets.UTF_8);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
            try {
                while (true) {
                    StringBuilder block = new StringBuilder();
                    type = new int[1];  // 1-specialSymbols  2-emphasis  3-strong 4-code 5 - strikeout 6 - pre
                    int typeLength = 0;
                    pos = new int[1];
                    markupIndex = new int[1];
                    String curString = sc.nextLine();
                    int preCnt = 0;
                    while (curString != null && curString.isEmpty()) {
                        curString = sc.nextLine();
                    }
                    if (curString == null) {
                        break;
                    }
                    while (curString != null && !curString.isEmpty()) {
                        block.append(curString);
                        block.append('\n');
                        curString = sc.nextLine();
                    }
                    int cnt = 0;
                    while (block.charAt(cnt) == '#') {
                        ++cnt;
                    }
                    if (!Character.isWhitespace(block.charAt(cnt)) && !(Character.getType(block.charAt(cnt)) == Character.CONTROL)) {
                        cnt = 0;
                    }
                    out.write((cnt == 0 ? "<p>" : "<h" + cnt + ">"));
                    String strBlock = block.substring((cnt == 0 ? cnt : cnt + 1));
                    pushToken(1, -1, 10, typeLength++);
                    for (int i = 0; i < strBlock.length() - 1; ++i) {
                        char curChar = strBlock.charAt(i);
                        if (curChar == '\\') {
                            pushToken(1, i, 10, typeLength++);
                            ++i;
                        } else if (curChar == '<' || curChar == '>' || curChar == '&') {
                            int curMarkupIndex = switch (curChar) {
                                case '<' -> 11;
                                case '>' -> 12;
                                case '&' -> 13;
                                default -> -1;
                            };
                            pushToken(1, i, curMarkupIndex, typeLength++);
                        } else if ((curChar == '*' && (strBlock.charAt(i + 1) != '*')) || (curChar == '_' && (strBlock.charAt(i + 1) != '_'))) {
                            pushToken(2, i, -1, typeLength++);
                        } else if (curChar == '*' || curChar == '_') {
                            pushToken(3, i, -1, typeLength++);
                            pushToken(1, i + 1, 10, typeLength++);
                            ++i;
                        } else if (i <= strBlock.length() - 3 && curChar == '`' && strBlock.charAt(i + 1) == '`' && strBlock.charAt(i + 2) == '`') {
                            pushToken(6, i, 10, typeLength++);
                            i += 2;
                            ++preCnt;
                        } else if (curChar == '`') {
                            pushToken(4, i, -1, typeLength++);
                        } else if (curChar == '-' && (strBlock.charAt(i + 1) == '-')) {
                            pushToken(5, i, -1, typeLength++);
                            pushToken(1, i + 1, 10, typeLength++);
                            ++i;
                        }
                    }
                    pushToken(1, strBlock.length() - 1, 10, typeLength++);
                    int[] stack = new int[1];
                    int stackLength = 0;
                    for (int i = 0; i < typeLength; ++i) {
                        if (stackLength != 0 && type[stack[stackLength - 1]] == type[i] && strBlock.charAt(pos[i]) == strBlock.charAt(pos[stack[stackLength - 1]])) {
                            markupIndex[stack[stackLength - 1]] = 2 * (type[stack[stackLength - 1]] - 2);
                            markupIndex[i] = 2 * (type[stack[stackLength - 1]] - 2) + 1;
                            stackLength--;
                        } else if (type[i] == 2 || type[i] == 3 || type[i] == 4 || type[i] == 5) {
                            stack = push(stack, stackLength++, i);
                        }
                    }
                    int curPreCnt = 0;
                    int move = 0;
                    for (int i = 1; i < typeLength; ++i) {
                        if (type[i - 1] == 6 && curPreCnt + 1 != preCnt) {
                            curPreCnt++;
                            out.write(markup[8]);
                            int pointerL = pos[i] + 2;
                            for (; type[i] != 6; ++i) {
                                if (type[i] == 1 && markupIndex[i] != 10) {
                                    out.write(strBlock.substring(pointerL, pos[i]) + markup[markupIndex[i]]);
                                    pointerL = pos[i] + 1;
                                }
                            }
                            out.write(strBlock.substring(pointerL, pos[i]) + markup[9]);
                            move = 2;
                        } else {
                            if (type[i - 1] == 6) {
                                ++curPreCnt;
                            }
                            String tail = (markupIndex[i] != -1 ? markup[markupIndex[i]] :
                                    (type[i] == 3 || type[i] == 5 ? strBlock.substring(pos[i], pos[i] + 2) : strBlock.substring(pos[i], pos[i] + 1)));
                            out.write(strBlock.substring(pos[i - 1] + 1 + move, pos[i]) + tail);
                            move = 0;
                        }
                    }
                    out.write((cnt == 0 ? "</p>" : "</h" + cnt + ">") + '\n');
                }
            } finally {
                sc.close();
                out.close();
            }
        } catch (
                IOException e) {
            System.out.print("error while reading or writing " + e.getMessage());
        }
    }
}
