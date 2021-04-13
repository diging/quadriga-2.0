package edu.asu.diging.quadriga.model.events;

import java.util.HashSet;
import java.util.Set;

import edu.asu.diging.quadriga.model.elements.Actor;
import edu.asu.diging.quadriga.model.elements.Element;
import edu.asu.diging.quadriga.model.elements.SourceReference;

public class CreationEvent extends Element {

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

}