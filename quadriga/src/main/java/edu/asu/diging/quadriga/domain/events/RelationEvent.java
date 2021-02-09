package edu.asu.diging.quadriga.domain.events;

import edu.asu.diging.quadriga.domain.elements.Actor;
import edu.asu.diging.quadriga.domain.elements.Relation;

public class RelationEvent extends CreationEvent {

    private Relation relation;

    private Actor relation_creator;

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Actor getRelationCreator() {
        return relation_creator;
    }

    public void setRelationCreator(Actor actor) {
        this.relation_creator = actor;
    }

}