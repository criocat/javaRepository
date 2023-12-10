package markup;

import java.util.List;

public class UnorderedList extends AbstractList {
    UnorderedList() {}
    public UnorderedList(List<ListItem> textElements) {
        super(textElements, "[list]", "[/list]");
    }
    public void toBBCode(StringBuilder outText) {
        super.toBBCode(outText);
    }
}