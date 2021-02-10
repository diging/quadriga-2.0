package edu.asu.diging.quadriga.domain.elements.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.elements.Relation;
import edu.asu.diging.quadriga.domain.elements.factory.IRelationFactory;

@Service
public class RelationFactory implements IRelationFactory {

    public Relation createRelation() {
        return new Relation();
    }
}