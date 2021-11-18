package edu.asu.diging.quadriga.core.conceptpower.reply.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type_id",
    "type_uri",
    "type_name"
})
public class Type {

    @JsonProperty("type_id")
    private String typeId;
    @JsonProperty("type_uri")
    private String typeUri;
    @JsonProperty("type_name")
    private String typeName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type_id")
    public String getTypeId() {
        return typeId;
    }

    @JsonProperty("type_id")
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @JsonProperty("type_uri")
    public String getTypeUri() {
        return typeUri;
    }

    @JsonProperty("type_uri")
    public void setTypeUri(String typeUri) {
        this.typeUri = typeUri;
    }

    @JsonProperty("type_name")
    public String getTypeName() {
        return typeName;
    }

    @JsonProperty("type_name")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
