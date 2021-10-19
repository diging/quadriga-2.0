package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
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
    public Page<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId, Pageable pageable) {
        return repo.findByCollectionIdOrderByCreationTimeDesc(collectionId, pageable).orElse(null);
    }

    @Override
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId) {
        return repo.findByCollectionIdOrderByCreationTimeDesc(collectionId)
                .map(eventGraphs -> eventGraphs.get(0))
                .orElse(null);
    }
    
    @Override
    public int findAllTriplesInEventGraph(ObjectId id) throws TriplesNotFoundException {
        Optional<EventGraph> optionalEventGraph = repo.findById(id);
        
        if(optionalEventGraph.isPresent()) {
            CreationEvent creationEvent = optionalEventGraph.get().getRootEvent();
            
            if(creationEvent != null && creationEvent instanceof RelationEvent) {
                
                Relation relation = ((RelationEvent) optionalEventGraph.get().getRootEvent()).getRelation();
                
                // Here, we first make sure that there is a root relation and that this root has a triple
                if(relation != null && relation.getSubject() != null && relation.getObject() != null && relation.getPredicate() != null) {
                    return getNumberOfTriples(relation, 0);
                } else {
                    throw new TriplesNotFoundException("No relation or triple found in the root RelationEvent of EventGraph with id: " + id.toString());
                }
            } else {
                throw new TriplesNotFoundException("No RelationEvent found in the rootEvent of EventGraph with id: " + id.toString());
            }
        } else {
            throw new TriplesNotFoundException("No EventGraph found for id: " + id.toString());
        }
    }
    
    /**
     * This method will recursively find triples in "subject" and "object" RelationEvents
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
                triples = getNumberOfTriples(subjectRelation, triples);
            }
        }
        if(relation.getObject() != null && relation.getObject() instanceof RelationEvent) {
            Relation objectRelation = ((RelationEvent) relation.getObject()).getRelation();
            if(objectRelation != null) {
                triples = getNumberOfTriples(objectRelation, triples);
            }
        }
        return ++triples;
    }
    
}
