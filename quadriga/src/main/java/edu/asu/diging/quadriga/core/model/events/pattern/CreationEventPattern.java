package edu.asu.diging.quadriga.core.model.events.pattern;

public class CreationEventPattern extends BaseEventPattern {

    private String conceptType;

    public String getConceptType() {
        return conceptType;
    }

    public void setConceptType(String conceptType) {
        this.conceptType = conceptType;
    }
}
