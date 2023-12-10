package markup;

import java.util.ArrayList;
import java.util.List;

public class Paragraph implements isParagrafOrList {
    List<ToMarkdownAndBBCode> textElements = new ArrayList<>();
    public Paragraph() {}
    public Paragraph(List<ToMarkdownAndBBCode> textElements) {
        this.textElements = List.copyOf(textElements);
    }
    public void toMarkdown(StringBuilder outText) {
        for (ToMarkdownAndBBCode element : textElements) {
            element.toMarkdown(outText);
        }
    }
    public void toBBCode(StringBuilder outText) {
        for (ToMarkdownAndBBCode element : textElements) {
            element.toBBCode(outText);
        }
    }
}
