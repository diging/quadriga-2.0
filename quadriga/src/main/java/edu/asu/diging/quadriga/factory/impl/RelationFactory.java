package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.IRelationFactory;
import edu.asu.diging.quadriga.model.elements.Relation;
@Deprecated
@Service
public class RelationFactory implements IRelationFactory {

    public Relation createRelation() {
        return new Relation();
    }
}