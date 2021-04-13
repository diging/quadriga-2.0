package edu.asu.diging.quadriga.model.events;

import edu.asu.diging.quadriga.model.elements.Term;

public class AppellationEvent extends CreationEvent {

    private Term term;

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
