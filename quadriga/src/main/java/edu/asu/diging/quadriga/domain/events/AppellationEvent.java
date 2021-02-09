package edu.asu.diging.quadriga.domain.events;

import edu.asu.diging.quadriga.domain.elements.Term;

public class AppellationEvent extends CreationEvent {

    private Term term;

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
