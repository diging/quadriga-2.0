package edu.asu.diging.quadriga.core.pattern;

public class PatternRelationEvent extends PatternCreationEvent {

    private PatternCreationEvent subject;

    private PatternCreationEvent object;

    private PatternAppellationEvent predicate;

    public PatternCreationEvent getSubject() {
        return subject;
    }

    public void setSubject(PatternCreationEvent subject) {
        this.subject = subject;
    }

    public PatternCreationEvent getObject() {
        return object;
    }

    public void setObject(PatternCreationEvent object) {
        this.object = object;
    }

    public PatternAppellationEvent getPredicate() {
        return predicate;
    }

    public void setPredicate(PatternAppellationEvent predicate) {
        this.predicate = predicate;
    }

}
