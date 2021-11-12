
package edu.asu.diging.quadriga.core.conceptpower.reply.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "conceptEntries",
    "pagination"
})
public class ConceptPowerReply {

    @JsonProperty("conceptEntries")
    private List<ConceptEntry> conceptEntries = null;
    @JsonProperty("pagination")
    private Object pagination;

    @JsonProperty("conceptEntries")
    public List<ConceptEntry> getConceptEntries() {
        return conceptEntries;
    }

    @JsonProperty("conceptEntries")
    public void setConceptEntries(List<ConceptEntry> conceptEntries) {
        this.conceptEntries = conceptEntries;
    }

    @JsonProperty("pagination")
    public Object getPagination() {
        return pagination;
    }

    @JsonProperty("pagination")
    public void setPagination(Object pagination) {
        this.pagination = pagination;
    }

}
