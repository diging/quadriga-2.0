package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.exceptions.TriplesNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Service
public class EventGraphServiceImpl implements EventGraphService {

    @Autowired
    private EventGraphRepository repo;

    @Override
    public void saveEventGraphs(List<EventGraph> events) {
        for (EventGraph event : events) {
            repo.save(event);
        }
    }

    @Override
    public List<EventGraph> findAllEventGraphsByCollectionId(String collectionId) {
        return repo.findByCollectionIdOrderByCreationTimeDesc(new ObjectId(collectionId)).orElse(null);
    }

    @Override
    public int findAllTriplesInEventGraph(ObjectId id) throws TriplesNotFoundException {
        Optional<EventGraph> optionalEventGraph = repo.findById(id);
        
        if(optionalEventGraph.isPresent()) {
            CreationEvent creationEvent = optionalEventGraph.get().getRootEvent();
            if(creationEvent != null && creationEvent instanceof RelationEvent) {
                return getNumberOfTriples(((RelationEvent) optionalEventGraph.get().getRootEvent()).getRelation(), 1);
            }
        }
        
        // If no eventGraph was found for the given Id or in the EventGraph, there are no triples present
        throw new TriplesNotFoundException();
    }
    
    /**
     * This method will recursively find triples in "subject" and "object" type of RelationEvents
     * E.g. if a relation with subject-predicate-object triple has another nested relation
     * inside object of this triple, then the outer and inner triple will count as 2 triples and
     * 2 will be returned
     * 
     * @param relation is the root relation of the network (i.e. eventGraph)
     * @param triples will be the recursively counted value of triples for the network
     * @return the number of triples
     */
    private int getNumberOfTriples(Relation relation, int triples) {
        if(relation.getSubject() != null && relation.getSubject() instanceof RelationEvent) {
            Relation subjectRelation = ((RelationEvent) relation.getSubject()).getRelation();
            if(subjectRelation != null) {
                return getNumberOfTriples(subjectRelation, ++triples);
            }
        }
        if(relation.getObject() != null && relation.getObject() instanceof RelationEvent) {
            Relation objectRelation = ((RelationEvent) relation.getObject()).getRelation();
            if(objectRelation != null) {
                return getNumberOfTriples(objectRelation, ++triples);
            }
        }
        return triples;
    }
    
}
