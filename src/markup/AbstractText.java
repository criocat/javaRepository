package markup;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractText implements ToMarkdownAndBBCode {
    String startMarkup;
    String endMarkup;
    String startMarkupBBCode;
    String endMarkupBBCode;
    List<ToMarkdownAndBBCode> textElements = new ArrayList<>();
    public AbstractText() {}
    public AbstractText(List<ToMarkdownAndBBCode> textElements, String startMarkup, String endMarkup, String startMarkupBBCode, String endMarkupBBCode) {
        this.textElements = List.copyOf(textElements);
        this.startMarkup = startMarkup;
        this.endMarkup = endMarkup;
        this.startMarkupBBCode = startMarkupBBCode;
        this.endMarkupBBCode = endMarkupBBCode;
    }
    public void toMarkdown(StringBuilder outText) {
        outText.append(startMarkup);
        for (ToMarkdownAndBBCode element : textElements) {
            element.toMarkdown(outText);
        }
        outText.append(endMarkup);
    }
    public void toBBCode(StringBuilder outText) {
        outText.append(startMarkupBBCode);
        for (ToMarkdownAndBBCode element : textElements) {
            element.toBBCode(outText);
        }
        outText.append(endMarkupBBCode);
    }
}
