package game;

public enum Cell {
    X("X"), O("O"), E(".");
    private final String field;
    Cell(String field) {
        this.field = field;
    }
    public String getField() {
        return field;
    }
}
