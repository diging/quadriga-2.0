package edu.asu.diging.quadriga.legacy.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
@Deprecated
public class Message {
    private static final String RETURN_JSON = "application/json";

    private String message;

    private Map<String, String> messageMap = new HashMap<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(Map<String, String> messageMap) {
        this.messageMap = messageMap;
    }

    public String toString(String type) {

        if (type != null && type.equals(RETURN_JSON)) {
            StringBuilder sb = new StringBuilder();
            sb.append("{ \"message\" : ");

            if (!messageMap.isEmpty()) {
                sb.append("{ ");
                sb.append(messageMap.entrySet().stream().map(this::mapToJSON).collect(Collectors.joining(" , ")));
                sb.append(" }");
                sb.append(" }");
                return sb.toString();
            }
            sb.append("\"" + message + "\" }");
            return sb.toString();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<message>");
        if (!messageMap.isEmpty()) {
            sb.append(messageMap.entrySet().stream().map(this::mapToXML).collect(Collectors.joining()));
            sb.append("</message>");
            return sb.toString();
        }
        sb.append(message);
        sb.append("</message>");
        return sb.toString();
    }

    private String mapToJSON(Entry<String, String> entry) {
        return "\"" + entry.getKey() + "\" : "
                + (entry.getValue().startsWith("{") ? entry.getValue() : "\"" + entry.getValue() + "\"");
    }

    private String mapToXML(Entry<String, String> entry) {
        return "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + "> ";
    }

}