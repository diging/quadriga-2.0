package edu.asu.diging.quadriga.core.model;

/**
 * Holds the data of a mapped triple extracted from the submitted network.
 * @author Maulik Limbadiya
 *
 */
public class Triple {
    
    private TripleElement subject;
    
    private TripleElement object;
    
    private TripleElement predicate;
    
    public TripleElement getSubject() {
        return subject;
    }

    public void setSubject(TripleElement subject) {
        this.subject = subject;
    }

    public TripleElement getObject() {
        return object;
    }

    public void setObject(TripleElement object) {
        this.object = object;
    }

    public TripleElement getPredicate() {
        return predicate;
    }

    public void setPredicate(TripleElement predicate) {
        this.predicate = predicate;
    }
    
}
