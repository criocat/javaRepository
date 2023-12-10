package markup;

public class Text implements ToMarkdownAndBBCode {
    StringBuilder text;
    Text() {}
    Text(String str) {
        text = new StringBuilder(str);
    }
    public void toMarkdown(StringBuilder outText) {
        outText.append(text.toString());
    }
    public void toBBCode(StringBuilder outText) {
        outText.append(text.toString());
    }
}
