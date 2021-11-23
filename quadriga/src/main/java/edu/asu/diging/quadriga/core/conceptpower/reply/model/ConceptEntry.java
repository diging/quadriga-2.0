
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

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("lemma")
    public String getLemma() {
        return lemma;
    }

    @JsonProperty("lemma")
    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("conceptList")
    public String getConceptList() {
        return conceptList;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(Type type) {
        this.type = type;
    }

    @JsonProperty("conceptList")
    public void setConceptList(String conceptList) {
        this.conceptList = conceptList;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("concept_uri")
    public String getConceptUri() {
        return conceptUri;
    }

    @JsonProperty("concept_uri")
    public void setConceptUri(String conceptUri) {
        this.conceptUri = conceptUri;
    }

    @JsonProperty("creator_id")
    public String getCreatorId() {
        return creatorId;
    }

    @JsonProperty("creator_id")
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @JsonProperty("equal_to")
    public String getEqualTo() {
        return equalTo;
    }

    @JsonProperty("equal_to")
    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    @JsonProperty("modified_by")
    public String getModifiedBy() {
        return modifiedBy;
    }

    @JsonProperty("modified_by")
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty("similar_to")
    public String getSimilarTo() {
        return similarTo;
    }

    @JsonProperty("similar_to")
    public void setSimilarTo(String similarTo) {
        this.similarTo = similarTo;
    }

    @JsonProperty("synonym_ids")
    public String getSynonymIds() {
        return synonymIds;
    }

    @JsonProperty("synonym_ids")
    public void setSynonymIds(String synonymIds) {
        this.synonymIds = synonymIds;
    }

    @JsonProperty("wordnet_id")
    public String getWordnetId() {
        return wordnetId;
    }

    @JsonProperty("wordnet_id")
    public void setWordnetId(String wordnetId) {
        this.wordnetId = wordnetId;
    }

    @JsonProperty("alternativeIds")
    public List<AlternativeId> getAlternativeIds() {
        return alternativeIds;
    }

    @JsonProperty("alternativeIds")
    public void setAlternativeIds(List<AlternativeId> alternativeIds) {
        this.alternativeIds = alternativeIds;
    }

}
