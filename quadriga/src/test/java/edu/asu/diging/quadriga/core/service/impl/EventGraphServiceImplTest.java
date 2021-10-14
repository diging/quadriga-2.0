package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.exceptions.TriplesNotFoundException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;

public class EventGraphServiceImplTest {

    @Mock
    private EventGraphRepository eventGraphRepository;

    @InjectMocks
    private EventGraphServiceImpl eventGraphServiceImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_findAllEventGraphsByCollectionId_success() throws InterruptedException {
        ObjectId collectionObjectId = new ObjectId();

        EventGraph eventGraph1 = new EventGraph();
        ObjectId eventGraphObjectId1 = new ObjectId();

        eventGraph1.setId(eventGraphObjectId1);
        eventGraph1.setCollectionId(collectionObjectId);

        EventGraph eventGraph2 = new EventGraph();
        ObjectId eventGraphObjectId2 = new ObjectId();

        eventGraph2.setId(eventGraphObjectId2);
        eventGraph2.setCollectionId(collectionObjectId);

        // As per this list, eventGraph2 was created after eventGraph1
        List<EventGraph> eventGraphs = new ArrayList<EventGraph>();
        eventGraphs.add(eventGraph2);
        eventGraphs.add(eventGraph1);

        Mockito.when(eventGraphRepository.findByCollectionIdOrderByCreationTimeDesc(collectionObjectId, PageRequest.of(0, 10)))
                .thenReturn(Optional.of(new PageImpl<EventGraph>(eventGraphs)));

        List<EventGraph> foundEventGraphs = new ArrayList<>();
        eventGraphServiceImpl.findAllEventGraphsByCollectionId(collectionObjectId, PageRequest.of(0, 10))
                            .forEach(eventGraph -> {
                                foundEventGraphs.add(eventGraph);
                            });
        
        // The latest, i.e. EventGraph2, will be the 1st one on the list
        Assert.assertEquals(eventGraphObjectId2, foundEventGraphs.get(0).getId());
        Assert.assertEquals(eventGraphObjectId1, foundEventGraphs.get(1).getId());
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noEventGraph() {
        ObjectId eventGraphId = new ObjectId();
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.ofNullable(null));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noRelationEvent() {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noRootRelationInRelationEvent() {
        RelationEvent relationEvent = new RelationEvent();
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        eventGraph.setRootEvent(relationEvent);
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noSubjectInRootRelation() {
        RelationEvent relationEvent = new RelationEvent();
        Relation relation = new Relation();
        relation.setObject(new AppellationEvent());
        relation.setPredicate(new AppellationEvent());
        relationEvent.setRelation(relation);
        
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        eventGraph.setRootEvent(relationEvent);
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noPredicateInRootRelation() {
        RelationEvent relationEvent = new RelationEvent();
        Relation relation = new Relation();
        relation.setSubject(new AppellationEvent());
        relation.setObject(new AppellationEvent());
        relationEvent.setRelation(relation);
        
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        eventGraph.setRootEvent(relationEvent);
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_noObjectInRootRelation() {
        RelationEvent relationEvent = new RelationEvent();
        Relation relation = new Relation();
        relation.setSubject(new AppellationEvent());
        relation.setPredicate(new AppellationEvent());
        relationEvent.setRelation(relation);
        
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        eventGraph.setRootEvent(relationEvent);
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        Assert.assertThrows(TriplesNotFoundException.class, () -> eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_oneTriple() throws TriplesNotFoundException {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        
        Relation relation = new Relation();
        AppellationEvent subject = new AppellationEvent();
        AppellationEvent predicate = new AppellationEvent();
        AppellationEvent object = new AppellationEvent();
        
        relation.setSubject(subject);
        relation.setPredicate(predicate);
        relation.setObject(object);
        
        RelationEvent relationEvent = new RelationEvent();
        relationEvent.setRelation(relation);
        
        eventGraph.setRootEvent(relationEvent);
        
        Assert.assertEquals(1, eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_twoTriples_oneNestedInSubject() throws TriplesNotFoundException {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        
        Relation nestedRelation = new Relation();
        AppellationEvent nestedSubject = new AppellationEvent();
        AppellationEvent nestedPredicate = new AppellationEvent();
        AppellationEvent nestedObject = new AppellationEvent();
        nestedRelation.setSubject(nestedSubject);
        nestedRelation.setPredicate(nestedPredicate);
        nestedRelation.setObject(nestedObject);
        
        RelationEvent subject = new RelationEvent();
        subject.setRelation(nestedRelation);
        
        Relation rootRelation = new Relation();
        rootRelation.setSubject(subject);
        
        AppellationEvent predicate = new AppellationEvent();
        AppellationEvent object = new AppellationEvent();
        rootRelation.setPredicate(predicate);
        rootRelation.setObject(object);
        
        RelationEvent relationEvent = new RelationEvent();
        relationEvent.setRelation(rootRelation);
        
        eventGraph.setRootEvent(relationEvent);
        
        Assert.assertEquals(2, eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_twoTriples_oneNestedInObject() throws TriplesNotFoundException {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        
        Relation nestedRelation = new Relation();
        AppellationEvent nestedSubject = new AppellationEvent();
        AppellationEvent nestedPredicate = new AppellationEvent();
        AppellationEvent nestedObject = new AppellationEvent();
        nestedRelation.setSubject(nestedSubject);
        nestedRelation.setPredicate(nestedPredicate);
        nestedRelation.setObject(nestedObject);
        
        RelationEvent object = new RelationEvent();
        object.setRelation(nestedRelation);
        
        Relation rootRelation = new Relation();
        rootRelation.setObject(object);

        AppellationEvent subject = new AppellationEvent();
        AppellationEvent predicate = new AppellationEvent();
        rootRelation.setSubject(subject);
        rootRelation.setPredicate(predicate);
        
        RelationEvent relationEvent = new RelationEvent();
        relationEvent.setRelation(rootRelation);
        
        eventGraph.setRootEvent(relationEvent);
        
        Assert.assertEquals(2, eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_threeTriples_nestedInSubjectAndObject() throws TriplesNotFoundException {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        
        // Relation nested in object
        Relation nestedObjectRelation = new Relation();
        AppellationEvent nestedSubject1 = new AppellationEvent();
        AppellationEvent nestedPredicate1 = new AppellationEvent();
        AppellationEvent nestedObject1 = new AppellationEvent();
        nestedObjectRelation.setSubject(nestedSubject1);
        nestedObjectRelation.setPredicate(nestedPredicate1);
        nestedObjectRelation.setObject(nestedObject1);
        
        RelationEvent object = new RelationEvent();
        object.setRelation(nestedObjectRelation);
        
        // Relation nested in subject
        Relation nestedSubjectRelation = new Relation();
        AppellationEvent nestedSubject2 = new AppellationEvent();
        AppellationEvent nestedPredicate2 = new AppellationEvent();
        AppellationEvent nestedObject2 = new AppellationEvent();
        nestedSubjectRelation.setSubject(nestedSubject2);
        nestedSubjectRelation.setPredicate(nestedPredicate2);
        nestedSubjectRelation.setObject(nestedObject2);
        
        RelationEvent subject = new RelationEvent();
        subject.setRelation(nestedSubjectRelation);
        
        // Root relation whose subject has a triple and object has a triple
        Relation rootRelation = new Relation();
        AppellationEvent predicate = new AppellationEvent();
        rootRelation.setSubject(subject);
        rootRelation.setPredicate(predicate);
        rootRelation.setObject(object);
        
        RelationEvent relationEvent = new RelationEvent();
        relationEvent.setRelation(rootRelation);
        
        eventGraph.setRootEvent(relationEvent);
        
        Assert.assertEquals(3, eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    
    @Test
    public void test_findAllTriplesInEventGraph_threeTriples_twoNestedLevelsInSubject() throws TriplesNotFoundException {
        ObjectId eventGraphId = new ObjectId();
        EventGraph eventGraph = new EventGraph();
        eventGraph.setId(eventGraphId);
        
        Mockito.when(eventGraphRepository.findById(eventGraphId)).thenReturn(Optional.of(eventGraph));
        
        // Inner nested relation which has no further nested triples
        Relation nestedInnerRelation = new Relation();
        AppellationEvent nestedInnerSubject = new AppellationEvent();
        AppellationEvent nestedInnerPredicate = new AppellationEvent();
        AppellationEvent nestedInnerObject = new AppellationEvent();
        nestedInnerRelation.setSubject(nestedInnerSubject);
        nestedInnerRelation.setPredicate(nestedInnerPredicate);
        nestedInnerRelation.setObject(nestedInnerObject);
        
        RelationEvent nestedSubject = new RelationEvent();
        nestedSubject.setRelation(nestedInnerRelation);
        
        // Outer nested relation which has the inner nested triple
        Relation nestedOuterRelation = new Relation();
        AppellationEvent nestedOuterPredicate = new AppellationEvent();
        AppellationEvent nestedOuterObject = new AppellationEvent();
        nestedOuterRelation.setSubject(nestedSubject);
        nestedOuterRelation.setPredicate(nestedOuterPredicate);
        nestedOuterRelation.setObject(nestedOuterObject);
        
        RelationEvent subject = new RelationEvent();
        subject.setRelation(nestedOuterRelation);
        
        // The root relation that has the outer nested triple
        Relation rootRelation = new Relation();
        AppellationEvent predicate = new AppellationEvent();
        AppellationEvent object = new AppellationEvent();
        rootRelation.setSubject(subject);
        rootRelation.setPredicate(predicate);
        rootRelation.setObject(object);
        
        RelationEvent relationEvent = new RelationEvent();
        relationEvent.setRelation(rootRelation);
        
        eventGraph.setRootEvent(relationEvent);
        
        Assert.assertEquals(3, eventGraphServiceImpl.findAllTriplesInEventGraph(eventGraphId));
    }
    

}
