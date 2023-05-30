package edu.asu.diging.quadriga.core.conceptpower.reply.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "concept_id", "concept_uri" })
public class AlternativeId {

    @JsonProperty("concept_id")
    private String conceptId;
    @JsonProperty("concept_uri")
    private String conceptUri;

    
    public String getConceptId() {
        return conceptId;
    }

    
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    
    public String getConceptUri() {
        return conceptUri;
    }

    
    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

}
