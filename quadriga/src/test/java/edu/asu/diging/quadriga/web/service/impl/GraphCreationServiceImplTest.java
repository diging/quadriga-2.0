package edu.asu.diging.quadriga.web.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.asu.diging.quadriga.core.exceptions.ConceptpowerNoResponseException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.elements.Concept;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;

public class GraphCreationServiceImplTest {

    @Mock
    private ConceptPowerService conceptPowerService;

    @InjectMocks
    private GraphCreationServiceImpl graphCreationServiceImpl;

    private static List<List<EventGraph>> sampleEventGraphLists = new ArrayList<>();
    private static ConceptCache subject1, subject2;
    private static ConceptCache predicate1, predicate2;
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
    public void test_createGraph_singleLevelGraph_success() throws ConceptpowerNoResponseException {
        List<EventGraph> singleEventGraphList = sampleEventGraphLists.get(0);
        EventGraph eventGraph = singleEventGraphList.get(0);
        String[] eventGraphIds = new String[] { eventGraph.getId().toString() };

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

        GraphNodeData predicateNode = (GraphNodeData) nodes.get(0).getData();
        GraphNodeData subjectNode = (GraphNodeData) nodes.get(1).getData();
        GraphNodeData objectNode = (GraphNodeData) nodes.get(2).getData();

        Assert.assertEquals(1, subjectNode.getGroup());
        Assert.assertEquals(0, predicateNode.getGroup());
        Assert.assertEquals(1, objectNode.getGroup());

        Assert.assertEquals("Albert Einstein", subjectNode.getLabel());
        Assert.assertEquals("married", predicateNode.getLabel());
        Assert.assertEquals("Elsa Einstein", objectNode.getLabel());

        Assert.assertEquals("Albert Einstein was a German-born theoretical physicist", subjectNode.getDescription());
        Assert.assertEquals("a person who is married; \"we invited several young marrieds\"",
                predicateNode.getDescription());
        Assert.assertEquals("Elsa Einstein was the second wife and cousin of Albert Einstein",
                objectNode.getDescription());

        Assert.assertEquals(subjectSourceURI, subjectNode.getUri());
        Assert.assertEquals(predicateSourceURI, predicateNode.getUri());
        Assert.assertEquals(objectSourceURI, objectNode.getUri());

        Assert.assertArrayEquals(eventGraphIds, subjectNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, predicateNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, objectNode.getEventGraphIds().toArray());

        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/albert-einstein",
                    "http://www.digitalhps.org/concepts/albert-einstein-alt" },
                subjectNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/WID-10295819-N-01-married",
                    "http://www.digitalhps.org/concepts/WID-10295819-N-02-married" },
                predicateNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/elsa-einstein",
                    "http://www.digitalhps.org/concepts/elsa-einstein-alt" },
                objectNode.getAlternativeUris().toArray());
    }

    @Test
    public void test_createGraph_nestedEventGraph_success() throws ConceptpowerNoResponseException {
        List<EventGraph> nestedEventGraphList = sampleEventGraphLists.get(1);
        EventGraph eventGraph = nestedEventGraphList.get(0);
        String[] eventGraphIds = new String[] { eventGraph.getId().toString() };

        String subjectSourceURI = "http://www.digitalhps.org/concepts/albert-einstein";
        String predicateSourceURI = "http://www.digitalhps.org/concepts/married";
        String subjectSourceURI2 = "http://www.digitalhps.org/concepts/cousin";
        String predicateSourceURI2 = "http://www.digitalhps.org/concepts/be";
        String objectSourceURI = "http://www.digitalhps.org/concepts/elsa-einstein";

        Mockito.when(conceptPowerService.getConceptByUri(subjectSourceURI)).thenReturn(subject1);
        Mockito.when(conceptPowerService.getConceptByUri(predicateSourceURI)).thenReturn(predicate1);
        Mockito.when(conceptPowerService.getConceptByUri(objectSourceURI)).thenReturn(object1);
        Mockito.when(conceptPowerService.getConceptByUri(subjectSourceURI2)).thenReturn(subject2);
        Mockito.when(conceptPowerService.getConceptByUri(predicateSourceURI2)).thenReturn(predicate2);

        GraphElements graphElements = graphCreationServiceImpl.createGraph(nestedEventGraphList);

        List<GraphElement> nodes = graphElements.getNodes();
        List<GraphElement> edges = graphElements.getEdges();

        Assert.assertEquals(5, nodes.size());
        Assert.assertEquals(4, edges.size());
        nodes.forEach(node -> Assert.assertNotNull(node.getData()));
        edges.forEach(edge -> Assert.assertNotNull(edge.getData()));

        GraphNodeData predicateNode = (GraphNodeData) nodes.get(0).getData();
        GraphNodeData subjectNode = (GraphNodeData) nodes.get(1).getData();
        GraphNodeData predicateNode2 = (GraphNodeData) nodes.get(2).getData();
        GraphNodeData subjectNode2 = (GraphNodeData) nodes.get(3).getData();
        GraphNodeData objectNode = (GraphNodeData) nodes.get(4).getData();

        Assert.assertEquals(1, subjectNode.getGroup());
        Assert.assertEquals(0, predicateNode.getGroup());
        Assert.assertEquals(1, objectNode.getGroup());
        Assert.assertEquals(1, subjectNode2.getGroup());
        Assert.assertEquals(0, predicateNode2.getGroup());

        Assert.assertEquals("Albert Einstein", subjectNode.getLabel());
        Assert.assertEquals("married", predicateNode.getLabel());
        Assert.assertEquals("cousin", subjectNode2.getLabel());
        Assert.assertEquals("be", predicateNode2.getLabel());
        Assert.assertEquals("Elsa Einstein", objectNode.getLabel());

        Assert.assertEquals("Albert Einstein was a German-born theoretical physicist", subjectNode.getDescription());
        Assert.assertEquals("a person who is married; \"we invited several young marrieds\"",
                predicateNode.getDescription());
        Assert.assertEquals("the child of your aunt or uncle", subjectNode2.getDescription());
        Assert.assertEquals(
                "be identical to; be someone or something; \"The president of the company is John Smith\"; \"This is my house\"",
                predicateNode2.getDescription());
        Assert.assertEquals("Elsa Einstein was the second wife and cousin of Albert Einstein",
                objectNode.getDescription());

        Assert.assertEquals(subjectSourceURI, subjectNode.getUri());
        Assert.assertEquals(predicateSourceURI, predicateNode.getUri());
        Assert.assertEquals(subjectSourceURI2, subjectNode2.getUri());
        Assert.assertEquals(predicateSourceURI2, predicateNode2.getUri());
        Assert.assertEquals(objectSourceURI, objectNode.getUri());

        Assert.assertArrayEquals(eventGraphIds, subjectNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, predicateNode.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, subjectNode2.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, predicateNode2.getEventGraphIds().toArray());
        Assert.assertArrayEquals(eventGraphIds, objectNode.getEventGraphIds().toArray());

        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/albert-einstein",
                    "http://www.digitalhps.org/concepts/albert-einstein-alt" },
                subjectNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/WID-10295819-N-01-married",
                    "http://www.digitalhps.org/concepts/WID-10295819-N-02-married" },
                predicateNode.getAlternativeUris().toArray());
        Assert.assertArrayEquals(new String[] { "http://www.digitalhps.org/concepts/cousin",
            "http://www.digitalhps.org/concepts/cousin-2" },
                subjectNode2.getAlternativeUris().toArray());
        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/be", "http://www.digitalhps.org/concepts/be-2" },
                predicateNode2.getAlternativeUris().toArray());
        Assert.assertArrayEquals(
                new String[] { "http://www.digitalhps.org/concepts/elsa-einstein",
                    "http://www.digitalhps.org/concepts/elsa-einstein-alt" },
                objectNode.getAlternativeUris().toArray());
    }

    @Test
    public void test_createSubjectOrObjectNode_testUniqueNodesDoesntContain() throws ConceptpowerNoResponseException {
        List<GraphData> nodes = new ArrayList<>();
        Map<String, GraphNodeData> uniqueNodes = new HashMap<>();
        String sourceURI = "http://www.digitalhps.org/concepts/albert-einstein";
        AppellationEvent subject = (AppellationEvent) ((RelationEvent) (sampleEventGraphLists.get(0).get(0)
                .getRootEvent())).getRelation().getSubject();
        graphCreationServiceImpl.createSubjectOrObjectNode(nodes, subject, uniqueNodes, GraphNodeType.SUBJECT,
                new ObjectId().toString());
        Assert.assertTrue(uniqueNodes.containsKey(sourceURI));
        Assert.assertEquals(sourceURI, ((GraphNodeData) uniqueNodes.get(sourceURI)).getUri());
    }

    @Test
    public void test_createSubjectOrObjectNode_testUniqueNodesContains() throws ConceptpowerNoResponseException {
        List<GraphData> nodes = new ArrayList<>();
        Map<String, GraphNodeData> uniqueNodes = new HashMap<>();
        String sourceURI = "http://www.digitalhps.org/concepts/albert-einstein";

        GraphNodeData node = new GraphNodeData();
        node.setUri(sourceURI);
        ObjectId eventGraphId1 = new ObjectId();
        ObjectId eventGraphId2 = new ObjectId();
        ObjectId nodeId = new ObjectId();
        node.setId(nodeId.toString());

        ArrayList<String> eventGraphIds = new ArrayList<>();
        eventGraphIds.add(eventGraphId1.toString());
        node.setEventGraphIds(eventGraphIds);

        uniqueNodes.put(sourceURI, node);

        AppellationEvent subject = (AppellationEvent) ((RelationEvent) (sampleEventGraphLists.get(0).get(0)
                .getRootEvent())).getRelation().getSubject();

        graphCreationServiceImpl.createSubjectOrObjectNode(nodes, subject, uniqueNodes, GraphNodeType.SUBJECT,
                eventGraphId2.toString());

        GraphNodeData foundNode = (GraphNodeData) uniqueNodes.get(sourceURI);
        Assert.assertTrue(uniqueNodes.containsKey(sourceURI));
        Assert.assertEquals(sourceURI, foundNode.getUri());
        Assert.assertEquals(eventGraphId1.toString(), foundNode.getEventGraphIds().get(0));
        Assert.assertEquals(eventGraphId2.toString(), foundNode.getEventGraphIds().get(1));
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

        RelationEvent rootEvent2 = new RelationEvent();
        RelationEvent object2 = new RelationEvent();
        Relation baseRelation = new Relation();
        Relation objectRelation = new Relation();

        AppellationEvent subject2 = new AppellationEvent();
        Term termSubject2 = new Term();
        Concept interpretationSubject2 = new Concept();
        interpretationSubject2.setSourceURI("http://www.digitalhps.org/concepts/cousin");
        termSubject2.setInterpretation(interpretationSubject2);
        subject2.setTerm(termSubject2);

        AppellationEvent predicate2 = new AppellationEvent();
        Term termPredicate2 = new Term();
        Concept interpretationPredicate2 = new Concept();
        interpretationPredicate2.setSourceURI("http://www.digitalhps.org/concepts/be");
        termPredicate2.setInterpretation(interpretationPredicate2);
        predicate2.setTerm(termPredicate2);

        objectRelation.setSubject(subject2);
        objectRelation.setPredicate(predicate2);
        objectRelation.setObject(object);
        object2.setRelation(objectRelation);
        baseRelation.setSubject(subject);
        baseRelation.setPredicate(predicate);
        baseRelation.setObject(object2);
        rootEvent2.setRelation(baseRelation);

        EventGraph eventGraph2 = new EventGraph(rootEvent2);
        eventGraph2.setId(new ObjectId());

        List<EventGraph> nestedEventGraphList = Collections.singletonList(eventGraph2);
        sampleEventGraphs.add(nestedEventGraphList);

    }

    private static void createSampleConceptCache() {

        subject1 = new ConceptCache();
        subject1.setUri("http://www.digitalhps.org/concepts/albert-einstein");
        subject1.setWord("Albert Einstein");
        subject1.setDescription("Albert Einstein was a German-born theoretical physicist");
        subject1.setAlternativeUris(Arrays.asList("http://www.digitalhps.org/concepts/albert-einstein",
                "http://www.digitalhps.org/concepts/albert-einstein-alt"));

        predicate1 = new ConceptCache();
        predicate1.setUri("http://www.digitalhps.org/concepts/WID-10295819-N-01-married");
        predicate1.setWord("married");
        predicate1.setDescription("a person who is married; \"we invited several young marrieds\"");
        predicate1.setAlternativeUris(Arrays.asList("http://www.digitalhps.org/concepts/WID-10295819-N-01-married",
                "http://www.digitalhps.org/concepts/WID-10295819-N-02-married"));

        object1 = new ConceptCache();
        object1.setWord("Elsa Einstein");
        object1.setUri("http://www.digitalhps.org/concepts/elsa-einstein");
        object1.setDescription("Elsa Einstein was the second wife and cousin of Albert Einstein");
        object1.setAlternativeUris(Arrays.asList("http://www.digitalhps.org/concepts/elsa-einstein",
                "http://www.digitalhps.org/concepts/elsa-einstein-alt"));

        subject2 = new ConceptCache();
        subject2.setUri("http://www.digitalhps.org/concepts/cousin");
        subject2.setWord("cousin");
        subject2.setDescription("the child of your aunt or uncle");
        subject2.setAlternativeUris(Arrays.asList("http://www.digitalhps.org/concepts/cousin",
                "http://www.digitalhps.org/concepts/cousin-2"));

        predicate2 = new ConceptCache();
        predicate2.setUri("http://www.digitalhps.org/concepts/be");
        predicate2.setWord("be");
        predicate2.setDescription(
                "be identical to; be someone or something; \"The president of the company is John Smith\"; \"This is my house\"");
        predicate2.setAlternativeUris(
                Arrays.asList("http://www.digitalhps.org/concepts/be", "http://www.digitalhps.org/concepts/be-2"));
    }

}
