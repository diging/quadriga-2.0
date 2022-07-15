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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import edu.asu.diging.quadriga.api.v1.model.MappedTriplesPage;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
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

    private String mappedTripleGroupId;
    private Concept source;
    private Concept target;
    private Predicate predicate;
    private Page<Predicate> collectionPredicates;

    private static final int PAGE = 1;
    private static final int PAGE_SIZE = 10;
    private static final String SOURCE_LABEL = "Albert Einstein";
    private static final String SOURCE_URI = "https://viaf.org/viaf/75121530/";
    private static final String TARGET_LABEL = "Elsa Einstein";
    private static final String TARGET_URI = "http://viaf.org/viaf/31600989";
    private static final String PREDICATE_LABEL = "married";
    private static final String PREDICATE_URI = "http://www.digitalhps.org/concepts/WID-10295819-N-01-married";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mappedTripleGroupId = "123456";

        source = new Concept();
        source.setMappedTripleGroupId(mappedTripleGroupId);
        source.setLabel(SOURCE_LABEL);
        source.setUri(SOURCE_URI);

        target = new Concept();
        target.setMappedTripleGroupId(mappedTripleGroupId);
        target.setLabel(TARGET_LABEL);
        target.setUri(TARGET_URI);

        predicate = new Predicate();
        predicate.setLabel(PREDICATE_LABEL);
        predicate.setRelationship(PREDICATE_URI);
        predicate.setMappedTripleGroupId(mappedTripleGroupId);
        predicate.setSource(source);
        predicate.setTarget(target);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(predicate);

        collectionPredicates = new PageImpl<>(predicates, PageRequest.of(PAGE - 1, PAGE_SIZE), 1);
    }

    @Test
    public void test_getMappedTriples_success() {
        Mockito.when(predicateRepo.findByMappedTripleGroupId(mappedTripleGroupId, PageRequest.of(PAGE - 1, PAGE_SIZE)))
                .thenReturn(Optional.of(collectionPredicates));

        MappedTriplesPage triplesPage = serviceToTest.getMappedTriples(mappedTripleGroupId, PAGE, PAGE_SIZE);
        List<DefaultMapping> triples = triplesPage.getTriples();
        Assert.assertEquals(1, triples.size());
        DefaultMapping triple = triples.get(0);

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
        Mockito.when(predicateRepo.findByMappedTripleGroupId(mappedTripleGroupId, PageRequest.of(PAGE - 1, PAGE_SIZE)))
                .thenReturn(Optional.of(new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 1), 0)));

        List<DefaultMapping> triples = serviceToTest.getMappedTriples(mappedTripleGroupId, PAGE, PAGE_SIZE)
                .getTriples();
        Assert.assertEquals(0, triples.size());
    }
}
