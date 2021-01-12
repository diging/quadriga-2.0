package edu.asu.diging.quadriga.domain.elements;

public class VocabularyEntry extends Element {

    private String term;
    private String sourceURI;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    
    
    public String getSourceURI() {
        return sourceURI;
    }

    public void setSourceURI(String sourceURI) {
        this.sourceURI = sourceURI;
    }

}