package markup;

import java.util.List;

public class Strong extends AbstractText {
    public Strong() {}
    public Strong(List<ToMarkdownAndBBCode> textElements) {
        super(textElements, "__", "__", "[b]", "[/b]");
    }
    public void toMarkdown(StringBuilder outText) {
        super.toMarkdown(outText);
    }
    public void toBBCode(StringBuilder outText) {
        super.toBBCode(outText);
    }
}