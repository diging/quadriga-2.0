package edu.asu.diging.quadriga.core.pattern;

public class PatternAppellationEvent extends PatternCreationEvent {

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
