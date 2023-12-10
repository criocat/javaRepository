package md2html;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockConverterMd2Html {

    public BlockConverterMd2Html() {
    }

    private enum Markup {
        EMPHASIS_ST("<em>"),
        EMPHASIS_END("</em>"),
        STRONG_ST("<strong>"),
        STRONG_END("</strong>"),
        CODE_ST("<code>"),
        CODE_END("</code>"),
        STRIKEOUT_ST("<s>"),
        STRIKEOUT_END("</s>"),
        INSERT_ST("<ins>"),
        INSERT_END("</ins>"),
        DELETE_ST("<del>"),
        DELETE_END("</del>"),
        EMPTY(""),
        LEFT_BRACKET("&lt;"),
        RIGHT_BRACKET("&gt;"),
        AMPERSAND("&amp;");
        private final String stringValue;

        private Markup(String stringValue) {
            this.stringValue = stringValue;
        }

        public String toString() {
            return stringValue;
        }
    }


    private enum TypeMarkup {
        STRONG(Markup.STRONG_ST, Markup.STRONG_END, new String[]{"**", "__"}),
        CODE(Markup.CODE_ST, Markup.CODE_END, new String[]{"`"}),
        STRIKEOUT(Markup.STRIKEOUT_ST, Markup.STRIKEOUT_END, new String[]{"--"}),
        INSERT(Markup.INSERT_ST, Markup.INSERT_END, new String[]{"<<", ">>"}),
        DELETE(Markup.DELETE_ST, Markup.DELETE_END, new String[]{"}}", "{{"}),
        SPECIAL_SYMBOL(null, null, new String[]{"<", ">", "&", "\\"}),
        EMPHASIS(Markup.EMPHASIS_ST, Markup.EMPHASIS_END, new String[]{"*", "_"});
        Markup startMarkup = null, endMarkup = null;
        String[] mdMarkups = null;

        private TypeMarkup() {
        }

        private TypeMarkup(Markup startMarkup, Markup endMarkup, String[] mdMarkups) {
            this.startMarkup = startMarkup;
            this.endMarkup = endMarkup;
            this.mdMarkups = mdMarkups;
        }
    }

    private static class Node {
        TypeMarkup type;
        Markup markup;
        int pos;

        public Node() {
        }

        public Node(TypeMarkup type, Markup markup, int pos) {
            this.type = type;
            this.markup = markup;
            this.pos = pos;
        }
    }

    private final TypeMarkup[] eachTypeMarkup = new TypeMarkup[]{TypeMarkup.INSERT, TypeMarkup.DELETE, TypeMarkup.STRONG, TypeMarkup.EMPHASIS, TypeMarkup.CODE, TypeMarkup.STRIKEOUT,
            TypeMarkup.SPECIAL_SYMBOL};

    private static int[] push(int[] arr, int arrLen, int val) {
        if (arr.length == arrLen) {
            arr = Arrays.copyOf(arr, 2 * arrLen);
        }
        arr[arrLen] = val;
        return arr;
    }

    private boolean checkType(TypeMarkup type, String strBlock, int pos) {
        for (String str : type.mdMarkups) {
            if (pos + str.length() < strBlock.length() && str.equals(strBlock.substring(pos, pos + str.length()))) {
                return true;
            }
        }
        return false;
    }

    public String mdBlockToHtmlBlock(String strBlock) {
        ArrayList<Node> markups = new ArrayList<>();
        markups.add(new Node(TypeMarkup.SPECIAL_SYMBOL, Markup.EMPTY, -1));

        parseMdBlock(strBlock, markups);

        markups.add(new Node(TypeMarkup.SPECIAL_SYMBOL, Markup.EMPTY, strBlock.length() - System.lineSeparator().length()));

        stackProcessing(strBlock, markups);
        return getHtmlBlock(strBlock, markups).toString();
    }

    private static StringBuilder getHtmlBlock(String strBlock, ArrayList<Node> markups) {
        StringBuilder HtmlBlock = new StringBuilder();
        for (int i = 1; i < markups.size(); ++i) {
            String tail = getSectionTail(strBlock, markups, i);
            HtmlBlock.append(strBlock, markups.get(i - 1).pos + 1, markups.get(i).pos);
            HtmlBlock.append(tail);
        }
        return HtmlBlock;
    }

    private static String getSectionTail(String strBlock, ArrayList<Node> markups, int i) {
        String tail;
        if (markups.get(i).markup != null) {
            tail = markups.get(i).markup.toString();
        } else {
            int curPos = markups.get(i).pos;
            tail = switch (markups.get(i).type) {
                case STRONG, DELETE, INSERT, STRIKEOUT -> strBlock.substring(curPos, curPos + 2);
                default -> strBlock.substring(curPos, curPos + 1);
            };
        }
        return tail;
    }

    private static void stackProcessing(String strBlock, ArrayList<Node> markups) {
        int[] stack = new int[1];
        int stackLength = 0;
        for (int i = 0; i < markups.size(); ++i) {
            if (good(strBlock, markups, stackLength, i, stack)) {
                markups.get(stack[stackLength - 1]).markup = markups.get(stack[stackLength - 1]).type.startMarkup;
                markups.get(i).markup = markups.get(i).type.endMarkup;
                stackLength--;
            } else if (markups.get(i).type != TypeMarkup.SPECIAL_SYMBOL) {
                stack = push(stack, stackLength++, i);
            }
        }
    }

    private static boolean good(String strBlock, ArrayList<Node> markups, int stackLength, int i, int[] stack) {
        boolean validBrackets = stackLength - 1 >= 0 && strBlock.charAt(markups.get(i).pos) == strBlock.charAt(markups.get(stack[stackLength - 1]).pos);
        if (stackLength - 1 >= 0 && markups.get(i).type == TypeMarkup.INSERT || markups.get(i).type == TypeMarkup.DELETE) {
            validBrackets = !validBrackets;
        }
        return validBrackets && stackLength != 0 && markups.get(stack[stackLength - 1]).type == markups.get(i).type;
    }

    private void parseMdBlock(String strBlock, ArrayList<Node> markups) {
        for (int i = 0; i < strBlock.length() - 1; ++i) {
            c1:
            for (TypeMarkup type : eachTypeMarkup) {
                for (String curString : type.mdMarkups) {
                    if (checkType(type, strBlock, i)) {
                        Markup curMarkup = identifyMarkup(strBlock, markups, type, i);
                        if (curMarkup == Markup.EMPTY) {
                            ++i;
                        }
                        if (curString.length() == 2) {
                            markups.add(new Node(TypeMarkup.SPECIAL_SYMBOL, Markup.EMPTY, i + 1));
                            ++i;
                        }
                        break c1;
                    }
                }
            }
        }
    }

    private Markup identifyMarkup(String strBlock, ArrayList<Node> markups, TypeMarkup type, int i) {
        char curChar = strBlock.charAt(i);
        Markup curMarkup = switchSpecialSymbol(curChar);
        markups.add(new Node(type, curMarkup, i));
        if (type == TypeMarkup.SPECIAL_SYMBOL && curChar == '\\') {
            ++i;
            char nextChar = strBlock.charAt(i);
            if (checkType(TypeMarkup.SPECIAL_SYMBOL, strBlock, i) && nextChar != '\\') {
                Markup nextMarkup = switchSpecialSymbol(nextChar);
                markups.add(new Node(TypeMarkup.SPECIAL_SYMBOL, nextMarkup, i));
            }
        }
        return curMarkup;
    }

    private static Markup switchSpecialSymbol(char curChar) {
        return switch (curChar) {
            case '\\' -> Markup.EMPTY;
            case '<' -> Markup.LEFT_BRACKET;
            case '>' -> Markup.RIGHT_BRACKET;
            case '&' -> Markup.AMPERSAND;
            default -> null;
        };
    }

}
