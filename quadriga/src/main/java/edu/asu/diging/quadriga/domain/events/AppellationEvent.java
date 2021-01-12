package edu.asu.diging.quadriga.domain.events;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.asu.diging.quadriga.domain.elements.Term;

@XmlRootElement
public class AppellationEvent extends CreationEvent {

    private Term term;

    @XmlElement
    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
