package markup;

import java.util.List;

public class Strikeout extends AbstractText {
    public Strikeout() {}
    public Strikeout(List<ToMarkdownAndBBCode> textElements) {
        super(textElements, "~", "~", "[s]", "[/s]");
    }
    public void toMarkdown(StringBuilder outText) {
        super.toMarkdown(outText);
    }
    public void toBBCode(StringBuilder outText) {
        super.toBBCode(outText);
    }
}