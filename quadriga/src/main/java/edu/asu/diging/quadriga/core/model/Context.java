package edu.asu.diging.quadriga.core.model;

public class Context {

	private String creator;

	private String creationTime;

	private String creationPlace;

	private String sourceUri;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreationPlace() {
        return creationPlace;
    }

    public void setCreationPlace(String creationPlace) {
        this.creationPlace = creationPlace;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

}
