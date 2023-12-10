package markup;

import java.util.ArrayList;
import java.util.List;

public class ListItem {
    List<isParagrafOrList> textElements = new ArrayList<>();
    public ListItem() {}
    public ListItem(List<isParagrafOrList> textElements) {
        this.textElements = List.copyOf(textElements);
    }
    public void toBBCode(StringBuilder outText) {
        outText.append("[*]");
        for (isParagrafOrList element : textElements) {
            element.toBBCode(outText);
        }
    }
}