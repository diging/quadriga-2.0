package edu.asu.diging.quadriga.core.model;

public class DefaultMapping {

	private TripleElement subject;

	private TripleElement predicate;

	private TripleElement object;

    public TripleElement getSubject() {
        return subject;
    }

    public void setSubject(TripleElement subject) {
        this.subject = subject;
    }

    public TripleElement getPredicate() {
        return predicate;
    }

    public void setPredicate(TripleElement predicate) {
        this.predicate = predicate;
    }

    public TripleElement getObject() {
        return object;
    }

    public void setObject(TripleElement object) {
        this.object = object;
    }

}
