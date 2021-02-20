package ws.slink.cp.model;

public enum StyledElement {
    GLANCE("glance"),
    LIST("list"),
    ISSUE("issue")
    ;
    private String value;
    public String title() {
        return this.value;
    }
    StyledElement(String value) {
        this.value = value;
    }
    public static StyledElement of(StyledElement value) {
        for(StyledElement element : StyledElement.values()) {
            if (element.value.equals(value))
                return element;
        }
        return null;
    }

}
