package markup;

import java.util.ArrayList;
import java.util.List;

public class MainMarkUp {

    public static void main(String[] args) {
        Paragraph paragraph = new Paragraph(List.of(new Strikeout(List.of(new Text("123")))));
        StringBuilder str = new StringBuilder();
        paragraph.toMarkdown(str);
        System.out.print(str.toString());
    }
}
