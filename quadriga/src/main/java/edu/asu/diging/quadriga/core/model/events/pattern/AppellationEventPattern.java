package edu.asu.diging.quadriga.core.model.events.pattern;

public class AppellationEventPattern extends CreationEventPattern {

    private String conceptType;
    
    private String interpretation;

    public String getConceptType() {
        return conceptType;
    }

    public void setConceptType(String conceptType) {
        this.conceptType = conceptType;
    }
    
    public String getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }
    
}
