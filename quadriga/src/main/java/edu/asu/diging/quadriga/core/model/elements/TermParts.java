package edu.asu.diging.quadriga.core.model.elements;

import java.util.HashSet;
import java.util.Set;

public class TermParts extends Element {

    private Set<TermPart> termParts;

    private SourceReference source_reference;

    public TermParts() {
        termParts = new HashSet<TermPart>();
    }

    public Set<TermPart> getTermParts() {
        return termParts;
    }

    public SourceReference getReferencedSource() {
        return source_reference;
    }

    public void setTermParts(Set<TermPart> parts) {
        this.termParts = parts;
    }

    public void setReferencedSource(SourceReference reference) {
        this.source_reference = reference;
    }

}
