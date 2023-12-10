package markup;

import java.util.ArrayList;

public abstract class AbstractList implements isParagrafOrList {
    String startMarkup;
    String endMarkup;
    java.util.List<ListItem> textElements = new ArrayList<>();

    AbstractList() {
    }

    public AbstractList(java.util.List<ListItem> textElements, String startMarkup, String endMarkup) {
        this.textElements = java.util.List.copyOf(textElements);
        this.startMarkup = startMarkup;
        this.endMarkup = endMarkup;
    }

    public void toBBCode(StringBuilder outText) {
        outText.append(startMarkup);
        for (ListItem element : textElements) {
            element.toBBCode(outText);
        }
        outText.append(endMarkup);
    }
}
