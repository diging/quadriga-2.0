package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;

public class NodeMetadata {

    private String type;
    private String interpretation;
    private List<TermPart> termParts;
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getInterpretation() {
        return interpretation;
    }
    public void setInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }
    public List<TermPart> getTermParts() {
        return termParts;
    }
    public void setTermParts(List<TermPart> termParts) {
        this.termParts = termParts;
    }
}
