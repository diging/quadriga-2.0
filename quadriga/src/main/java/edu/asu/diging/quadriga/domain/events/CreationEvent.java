package edu.asu.diging.quadriga.domain.events;

import java.util.HashSet;
import java.util.Set;

import edu.asu.diging.quadriga.domain.elements.Actor;
import edu.asu.diging.quadriga.domain.elements.Element;
import edu.asu.diging.quadriga.domain.elements.SourceReference;

public class CreationEvent extends Element {

    Long graphId;

    private SourceReference source_reference;

    private Set<CreationEvent> predecessors;

    private Actor interpretation_creator;

    public CreationEvent() {
        predecessors = new HashSet<CreationEvent>();
    }

    public SourceReference getSourceReference() {
        return source_reference;
    }

    public void setSourceReference(SourceReference reference) {
        this.source_reference = reference;
    }

    public Set<CreationEvent> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(Set<CreationEvent> predecessors) {
        this.predecessors = predecessors;
    }

    public Actor getInterpretationCreator() {
        return interpretation_creator;
    }

    public void setInterpretationCreator(Actor interpretationCreator) {
        this.interpretation_creator = interpretationCreator;
    }

    public Long getGraphId() {
        return graphId;
    }

    public void setGraphId(Long graphId) {
        this.graphId = graphId;
    }

}