package md2html;

import java_classes.MyScanner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Md2Html {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.print("not valid number of arguments");
            return;
        }
        int cnt = 0;
        PrintWriter writer = null;
        try {
            Scanner sc = new Scanner(new File(args[0]), "UTF-8");
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8));
                if (args[1].equals("test45.out")) writer = new PrintWriter("myout.txt", "UTF-8");
                try {
                    while (true) {
                        StringBuilder StringBlock = readMdBlock(sc);
                        if (StringBlock == null) break;
                        cnt += StringBlock.length();
                        int headerLevel = getHeaderLevel(StringBlock);
                        String HtmlBlock = new BlockConverterMd2Html().mdBlockToHtmlBlock(StringBlock.substring((headerLevel == 0 ? headerLevel : headerLevel + 1)));
                        writeBlock(out, headerLevel, HtmlBlock);
                    }
                    if (args[1].equals("test45.out")) writer.write("" + cnt);
                } finally {
                    out.close();
                    if(writer != null) writer.close();
                }
            } finally {
                sc.close();
            }
        } catch (IOException e) {
            System.out.print("error while reading " + e.getMessage());
        }
    }

    private static StringBuilder readMdBlock(Scanner sc) throws IOException {
        String curLine = "";
        StringBuilder StringBlock = new StringBuilder();
        while (sc.hasNextLine() && curLine.isEmpty()) {
            curLine = sc.nextLine();
        }
        if (!sc.hasNextLine() && curLine.isEmpty()) {
            return null;
        }
        StringBlock.append(curLine);
        StringBlock.append(System.lineSeparator());
        while (sc.hasNextLine()) {
            curLine = sc.nextLine();
            if (curLine.isEmpty()) break;
            StringBlock.append(curLine);
            StringBlock.append(System.lineSeparator());
        }
        return StringBlock;
    }

    private static void writeBlock(BufferedWriter out, int headerLevel, String HtmlBlock) throws IOException {
        out.write((headerLevel == 0 ? "<p>" : "<h" + headerLevel + ">"));
        out.write(HtmlBlock);
        out.write((headerLevel == 0 ? "</p>" : "</h" + headerLevel + ">"));
        out.write(System.lineSeparator());
    }

    private static int getHeaderLevel(StringBuilder StringBlock) {
        int headerLevel = 0;
        while (StringBlock.charAt(headerLevel) == '#') {
            ++headerLevel;
        }
        if (!Character.isWhitespace(StringBlock.charAt(headerLevel)) && !(Character.getType(StringBlock.charAt(headerLevel)) == Character.CONTROL)) {
            headerLevel = 0;
        }
        return headerLevel;
    }
}
