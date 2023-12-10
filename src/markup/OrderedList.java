package markup;

import java.util.List;

public class OrderedList extends AbstractList {
    OrderedList() {}
    public OrderedList(List<ListItem> textElements) {
        super(textElements, "[list=1]", "[/list]");
    }
    public void toBBCode(StringBuilder outText) {
        super.toBBCode(outText);
    }
}