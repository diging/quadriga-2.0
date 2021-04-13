package edu.asu.diging.quadriga.model.events;

import edu.asu.diging.quadriga.model.elements.Actor;
import edu.asu.diging.quadriga.model.elements.Relation;

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