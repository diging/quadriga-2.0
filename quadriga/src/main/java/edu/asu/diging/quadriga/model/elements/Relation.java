package edu.asu.diging.quadriga.model.elements;

import edu.asu.diging.quadriga.model.events.AppellationEvent;
import edu.asu.diging.quadriga.model.events.CreationEvent;

public class Relation extends Element {

    private Long graphId;

    private CreationEvent subject;

    private CreationEvent object;

    private AppellationEvent predicate;

    private SourceReference source_reference;

    public CreationEvent getSubject() {
        return subject;
    }

    public void setSubject(CreationEvent subject) {
        this.subject = subject;
    }

    public CreationEvent getObject() {
        return object;
    }

    public void setObject(CreationEvent object) {
        this.object = object;
    }

    public AppellationEvent getPredicate() {
        return predicate;
    }

    public void setPredicate(AppellationEvent predicate) {
        this.predicate = predicate;
    }

    public SourceReference getSourceReference() {
        return source_reference;
    }

    public void setSourceReference(SourceReference reference) {
        this.source_reference = reference;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

}