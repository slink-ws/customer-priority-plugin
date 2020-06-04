package ws.slink.atlassian.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JsonBuilder {

    private Map<String, Object> jsonMap = new ConcurrentHashMap<>();

    public JsonBuilder put(String key, Object value) {
        jsonMap.put(key, value);
        return this;
    }

    public String build() {
        return "{" + jsonMap.keySet()
            .stream()
            .map(k -> "\"" + k + "\":" + objectValue(jsonMap.get(k)))
            .collect(Collectors.joining(", "))
            + "}"
        ;
    }

    private String objectValue(Object value) {
        if (value instanceof JsonBuilder) {
            return ((JsonBuilder) value).build();
        } else if (value instanceof String) {
            return "\"" + value + "\"";
        } else {
            return value.toString();
        }
    }

}
