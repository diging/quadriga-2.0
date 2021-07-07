package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.legacy.factory.elements.IRelationFactory;
@Deprecated
@Service
public class RelationFactory implements IRelationFactory {

    public Relation createRelation() {
        return new Relation();
    }
}