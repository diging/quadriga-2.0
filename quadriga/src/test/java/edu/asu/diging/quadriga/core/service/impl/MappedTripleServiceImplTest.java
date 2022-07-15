package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public class MappedTripleServiceImplTest {

    @InjectMocks
    private MappedTripleServiceImpl serviceToTest;

    @Mock
    private ConceptRepository conceptRepo;

    @Mock
    private PredicateRepository predicateRepo;

    private String tripleGroupId;
    private Concept source;
    private Concept target;
    private Predicate predicate;
    private List<Predicate> collectionPredicates;

    private static final String SOURCE_LABEL = "Albert Einstein";
    private static final String SOURCE_URI = "https://viaf.org/viaf/75121530/";
    private static final String TARGET_LABEL = "Elsa Einstein";
    private static final String TARGET_URI = "http://viaf.org/viaf/31600989";
    private static final String PREDICATE_LABEL = "married";
    private static final String PREDICATE_URI = "http://www.digitalhps.org/concepts/WID-10295819-N-01-married";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        tripleGroupId = "123456";

        source = new Concept();
        source.setMappedTripleGroupId(tripleGroupId);
        source.setLabel(SOURCE_LABEL);
        source.setUri(SOURCE_URI);

        target = new Concept();
        target.setMappedTripleGroupId(tripleGroupId);
        target.setLabel(TARGET_LABEL);
        target.setUri(TARGET_URI);

        predicate = new Predicate();
        predicate.setLabel(PREDICATE_LABEL);
        predicate.setRelationship(PREDICATE_URI);
        predicate.setMappedTripleGroupId(tripleGroupId);
        predicate.setSource(source);
        predicate.setTarget(target);

        collectionPredicates = new ArrayList<>();
        collectionPredicates.add(predicate);
    }

    @Test
    public void test_getMappedTriples_success() {
        Mockito.when(predicateRepo.findByMappedTripleGroupId(tripleGroupId)).thenReturn(Optional.of(collectionPredicates));

        List<Triple> triples = serviceToTest.getMappedTriples(tripleGroupId);
        Assert.assertEquals(1, triples.size());
        Triple triple = triples.get(0);

        TripleElement subject = triple.getSubject();
        Assert.assertEquals(SOURCE_LABEL, subject.getLabel());
        Assert.assertEquals(SOURCE_URI, subject.getUri());

        TripleElement object = triple.getObject();
        Assert.assertEquals(TARGET_LABEL, object.getLabel());
        Assert.assertEquals(TARGET_URI, object.getUri());

        TripleElement predicate = triple.getPredicate();
        Assert.assertEquals(PREDICATE_LABEL, predicate.getLabel());
        Assert.assertEquals(PREDICATE_URI, predicate.getUri());
    }

    @Test
    public void test_getMappedTriples_empty() {
        Mockito.when(predicateRepo.findByMappedTripleGroupId(tripleGroupId)).thenReturn(Optional.of(new ArrayList<>()));

        List<Triple> triples = serviceToTest.getMappedTriples(tripleGroupId);
        Assert.assertEquals(0, triples.size());
    }
}
