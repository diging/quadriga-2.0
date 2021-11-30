package edu.asu.diging.quadriga.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerService;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Concept;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;

public class GraphCreationServiceImplTest {

    @Mock
    private ConceptPowerService conceptPowerService;

    @InjectMocks
    private GraphCreationServiceImpl graphCreationServiceImpl;

    private static List<List<EventGraph>> sampleEventGraphLists = new ArrayList<>();
    private static ConceptCache subject1;
    private static ConceptCache predicate1;
    private static ConceptCache object1;

    @BeforeClass
    public static void setUpOnceBeforeAllTests() {
        createSampleEventGraphs(sampleEventGraphLists);
        createSampleConceptCache();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_createGraph_success_oneSubjectObjectPredicate_oneEventGraph() {
        List<EventGraph> singleEventGraphList = sampleEventGraphLists.get(0);
        EventGraph eventGraph = singleEventGraphList.get(0);
        String[] eventGraphIds = new String[] {eventGraph.getId().toString()};
        
        String subjectSourceURI = "http://www.digitalhps.org/concepts/albert-einstein";
        String predicateSourceURI = "http://www.digitalhps.org/concepts/married";
        String objectSourceURI = "http://www.digitalhps.org/concepts/elsa-einstein";
        
        Mockito.when(conceptPowerService.getConceptByUri(subjectSourceURI)).thenReturn(subject1);
        Mockito.when(conceptPowerService.getConceptByUri(predicateSourceURI)).thenReturn(predicate1);
        Mockito.when(conceptPowerService.getConceptByUri(objectSourceURI)).thenReturn(object1);
        
        GraphElements graphElements = graphCreationServiceImpl.createGraph(singleEventGraphList);
        
        List<GraphElement> nodes = graphElements.getNodes();
        List<GraphElement> edges = graphElements.getEdges();
        
        Assert.assertEquals(3, nodes.size());
        Assert.assertEquals(2, edges.size());
        nodes.forEach(node -> Assert.assertNotNull(node.getData()));
        edges.forEach(edge -> Assert.assertNotNull(edge.getData()));
        
        GraphNodeData predicateNode = (GraphNodeData)nodes.get(0).getData();
        GraphNodeData subjectNode = (GraphNodeData)nodes.get(1).getData();
        GraphNodeData objectNode = (GraphNodeData)nodes.get(2).getData();
        
        Assert.assertEquals(1, subjectNode.getGroup());
        Assert.assertEquals(0, predicateNode.getGroup());
        Assert.assertEquals(1, objectNode.getGroup());
        Assert.assertEquals("Albert Einstein", subjectNode.getLabel());
        Assert.assertEquals("married", predicateNode.getLabel());
        Assert.assertEquals("Elsa Einstein", objectNode.getLabel());
        Assert.assertEquals("Albert Einstein was a German-born theoretical physicist", subjectNode.getDescription());
        Assert.assertEquals("a person who is married; \"we invited several young marrieds\"", predicateNode.getDescription());
        Assert.assertEquals("Elsa Einstein was the second wife and cousin of Albert Einstein", objectNode.getDescription());
        Assert.assertEquals(subjectSourceURI, subjectNode.getUri());
        Assert.assertEquals(predicateSourceURI, predicateNode.getUri());
        Assert.assertEquals(objectSourceURI, objectNode.getUri());
        Assert.assertArrayEquals(eventGraphIds, subjectNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, predicateNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, objectNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(new String[] {
                "http://www.digitalhps.org/concepts/albert-einstein",
                "http://www.digitalhps.org/concepts/albert-einstein-alt"
                }, subjectNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(new String[] {
                "http://www.digitalhps.org/concepts/WID-10295819-N-01-married",
                "http://www.digitalhps.org/concepts/WID-10295819-N-02-married"
                }, predicateNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(new String[] {
                "http://www.digitalhps.org/concepts/elsa-einstein",
                "http://www.digitalhps.org/concepts/elsa-einstein-alt"
                }, objectNode.getAlternativeUris().toArray());
    }

    private static void createSampleEventGraphs(List<List<EventGraph>> sampleEventGraphs) {

        RelationEvent rootEvent = new RelationEvent();
        Relation relation = new Relation();

        AppellationEvent subject = new AppellationEvent();
        Term term = new Term();
        Concept interpretation = new Concept();
        interpretation.setSourceURI("http://www.digitalhps.org/concepts/albert-einstein");
        term.setInterpretation(interpretation);
        subject.setTerm(term);
        
        AppellationEvent predicate = new AppellationEvent();
        Term term3 = new Term();
        Concept interpretation3 = new Concept();
        interpretation3.setSourceURI("http://www.digitalhps.org/concepts/married");
        term3.setInterpretation(interpretation3);
        predicate.setTerm(term3);

        AppellationEvent object = new AppellationEvent();
        Term term2 = new Term();
        Concept interpretation2 = new Concept();
        interpretation2.setSourceURI("http://www.digitalhps.org/concepts/elsa-einstein");
        term2.setInterpretation(interpretation2);
        object.setTerm(term2);

        relation.setSubject(subject);
        relation.setObject(object);
        relation.setPredicate(predicate);

        rootEvent.setRelation(relation);

        EventGraph eventGraph = new EventGraph(rootEvent);
        eventGraph.setId(new ObjectId());

        List<EventGraph> singleEventGraphList = Collections.singletonList(eventGraph);
        sampleEventGraphs.add(singleEventGraphList);

    }

    private static void createSampleConceptCache() {
        
        subject1 = new ConceptCache();
        subject1.setUri("http://www.digitalhps.org/concepts/albert-einstein");
        subject1.setWord("Albert Einstein");
        subject1.setDescription("Albert Einstein was a German-born theoretical physicist");
        subject1.setAlternativeUris(Arrays.asList(
                "http://www.digitalhps.org/concepts/albert-einstein",
                "http://www.digitalhps.org/concepts/albert-einstein-alt"));
        
        predicate1 = new ConceptCache();
        predicate1.setUri("http://www.digitalhps.org/concepts/WID-10295819-N-01-married");
        predicate1.setWord("married");
        predicate1.setDescription("a person who is married; \"we invited several young marrieds\"");
        predicate1.setAlternativeUris(Arrays.asList(
                "http://www.digitalhps.org/concepts/WID-10295819-N-01-married",
                "http://www.digitalhps.org/concepts/WID-10295819-N-02-married"));
        
        object1 = new ConceptCache();
        object1.setWord("Elsa Einstein");
        object1.setUri("http://www.digitalhps.org/concepts/elsa-einstein");
        object1.setDescription("Elsa Einstein was the second wife and cousin of Albert Einstein");
        object1.setAlternativeUris(Arrays.asList(
                "http://www.digitalhps.org/concepts/elsa-einstein",
                "http://www.digitalhps.org/concepts/elsa-einstein-alt"));
    }

}
