package edu.asu.diging.quadriga.core.model.events.pattern;

public class RelationEventPattern extends CreationEventPattern {

    private CreationEventPattern subject;

    private CreationEventPattern object;

    private AppellationEventPattern predicate;

    public CreationEventPattern getSubject() {
        return subject;
    }

    public void setSubject(CreationEventPattern subject) {
        this.subject = subject;
    }

    public CreationEventPattern getObject() {
        return object;
    }

    public void setObject(CreationEventPattern object) {
        this.object = object;
    }

    public AppellationEventPattern getPredicate() {
        return predicate;
    }

    public void setPredicate(AppellationEventPattern predicate) {
        this.predicate = predicate;
    }

}
