package edu.asu.diging.quadriga.domain.elements;

public class SourceReference extends Element {

    private Long graphId;

    private String sourceURI;

    public SourceReference() {
        sourceURI = "";
    }

    public String getSourceURI() {
        return sourceURI;
    }

    public void setSourceURI(String uri) {
        this.sourceURI = uri;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

}