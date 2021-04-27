package edu.asu.diging.quadriga.network.model;

public class Network {

    private String creator;

    private String creationDate;

    private String creationPlace;

    private String sourceReference;

    private String interpretationCreator;

    private String relationCreator;

    private NodeData defaultMapping;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationPlace() {
        return creationPlace;
    }

    public void setCreationPlace(String creationPlace) {
        this.creationPlace = creationPlace;
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public String getInterpretationCreator() {
        return interpretationCreator;
    }

    public void setInterpretationCreator(String interpretationCreator) {
        this.interpretationCreator = interpretationCreator;
    }

    public String getRelationCreator() {
        return relationCreator;
    }

    public void setRelationCreator(String relationCreator) {
        this.relationCreator = relationCreator;
    }

    public NodeData getDefaultMapping() {
        return defaultMapping;
    }

    public void setDefaultMapping(NodeData defaultMapping) {
        this.defaultMapping = defaultMapping;
    }

}
