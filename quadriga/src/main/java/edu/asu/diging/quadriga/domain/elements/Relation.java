package edu.asu.diging.quadriga.domain.elements;

import edu.asu.diging.quadriga.domain.events.AppellationEvent;
import edu.asu.diging.quadriga.domain.events.CreationEvent;

//@XmlRootElement
//@NodeEntity
public class Relation extends Element {

    //@GraphId
    Long graphId;
    
    //@Relationship(type="hasSubject", direction=Relationship.OUTGOING)
    private CreationEvent subject;
    
    //@Relationship(type="hasObject", direction=Relationship.OUTGOING)
    private CreationEvent object;
    
    //@Relationship(type="hasPredicate", direction=Relationship.OUTGOING)
    private AppellationEvent predicate;
    
    //@Property(name = "source_reference")
    //@Convert(SourceReferenceConverter.class)
    private SourceReference source_reference;
    
    //@XmlElement(type=CreationEvent.class)
    public CreationEvent getSubject() {
        return subject;
    }

    public void setSubject(CreationEvent subject) {
        this.subject = subject;
    }

    //@XmlElement(type=CreationEvent.class)
    public CreationEvent getObject() {
        return object;
    }

    public void setObject(CreationEvent object) {
        this.object = object;
    }

    //@XmlElement(type=AppellationEvent.class)
    public AppellationEvent getPredicate() {
        return predicate;
    }

    public void setPredicate(AppellationEvent predicate) {
        this.predicate = predicate;
    }

    //@XmlElement(type=SourceReference.class)
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