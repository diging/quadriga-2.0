
package edu.asu.diging.quadriga.core.conceptpower.reply.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This class represents the JSON response received from ConceptPower, with key
 * 'conceptPowerEntry' under conceptPowerReply
 * 
 * @author poojakulkarni
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "lemma", "pos", "description", "conceptList", "type", "deleted", "concept_uri", "creator_id",
    "equal_to", "modified_by", "similar_to", "synonym_ids", "wordnet_id", "alternativeIds" })
public class ConceptEntry {

    @JsonProperty("id")
    private String id;
    @JsonProperty("lemma")
    private String lemma;
    @JsonProperty("pos")
    private String pos;
    @JsonProperty("description")
    private String description;
    @JsonProperty("conceptList")
    private String conceptList;
    @JsonProperty("type")
    private Type type;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("concept_uri")
    private String conceptUri;
    @JsonProperty("creator_id")
    private String creatorId;
    @JsonProperty("equal_to")
    private String equalTo;
    @JsonProperty("modified_by")
    private String modifiedBy;
    @JsonProperty("similar_to")
    private String similarTo;
    @JsonProperty("synonym_ids")
    private String synonymIds;
    @JsonProperty("wordnet_id")
    private String wordnetId;
    @JsonProperty("alternativeIds")
    private List<AlternativeId> alternativeIds = null;

    
    public String getId() {
        return id;
    }

    
    public void setId(String id) {
        this.id = id;
    }

    
    public String getLemma() {
        return lemma;
    }

    
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    
    public String getPos() {
        return pos;
    }

    
    public void setPos(String pos) {
        this.pos = pos;
    }

    
    public String getDescription() {
        return description;
    }

    
    public void setDescription(String description) {
        this.description = description;
    }

    
    public String getConceptList() {
        return conceptList;
    }

    
    public Type getType() {
        return type;
    }

    
    public void setType(Type type) {
        this.type = type;
    }

    
    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    
    public Boolean getDeleted() {
        return deleted;
    }

    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    
    public String getConceptUri() {
        return conceptUri;
    }

    
    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    @JsonProperty("creator_id")
    public String getCreatorId() {
        return creatorId;
    }

    
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    
    public String getEqualTo() {
        return equalTo;
    }

    
    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    
    public String getModifiedBy() {
        return modifiedBy;
    }

    
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    
    public String getSimilarTo() {
        return similarTo;
    }

    
    public void setSimilarTo(String similarTo) {
        this.similarTo = similarTo;
    }

    
    public String getSynonymIds() {
        return synonymIds;
    }

    
    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }

    
    public String getWordnetId() {
        return wordnetId;
    }

    
    public void setWordnetId(String wordnetId) {
        this.wordnetId = wordnetId;
    }

    
    public List<AlternativeId> getAlternativeIds() {
        return alternativeIds;
    }

    
    public void setAlternativeIds(List<AlternativeId> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

}
