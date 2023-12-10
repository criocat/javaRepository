package markup;

import java.util.List;

public class Emphasis extends AbstractText {
    public Emphasis() {}
    public Emphasis(List<ToMarkdownAndBBCode> textElements) {
        super(textElements, "*", "*", "[i]", "[/i]");
    }
    public void toMarkdown(StringBuilder outText) {
        super.toMarkdown(outText);
    }
    public void toBBCode(StringBuilder outText) {
        super.toBBCode(outText);
    }
}