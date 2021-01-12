package edu.asu.diging.quadriga.domain.elements;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TermParts extends Element {

    Long graphId;
    
    private Set<TermPart> termParts;
    
    private SourceReference source_reference;
    
    public TermParts() {
        termParts = new HashSet<TermPart>();
    }
    
    @XmlElementRefs({ @XmlElementRef(type=TermPart.class)}) 
    public Set<TermPart> getTermParts() {
        return termParts;
    }

    @XmlElement(type=SourceReference.class)
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
