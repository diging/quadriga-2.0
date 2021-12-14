package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.MappingTypes;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Service
public class MappedTripleServiceImpl implements MappedTripleService {

    @Autowired
    private ConceptRepository conceptRepo;

    @Autowired
    private PredicateRepository predicateRepo;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.quadriga.core.service.impl.MappedTripleService#
     * storeMappedGraph(edu.asu.diging.quadriga.api.v1.model.Graph)
     */
    @Override
    public Predicate storeMappedGraph(Graph graph, MappedCollection mappedCollection, String eventGraphId) throws NodeNotFoundException {
        DefaultMapping mapping = graph.getMetadata().getDefaultMapping();
        if (mapping == null) {
            return null;
        }

        String mappedCollectionId = mappedCollection.get_id().toString();
        Map<String, NodeData> nodes = graph.getNodes();
        Concept subject = createConcept(mapping.getSubject(), nodes, mappedCollectionId, eventGraphId);
        subject = conceptRepo.save(subject);
        Concept object = createConcept(mapping.getObject(), nodes, mappedCollectionId, eventGraphId);
        object = conceptRepo.save(object);
        Predicate predicate = createPredicate(mapping.getPredicate(), nodes, subject, object, mappedCollectionId, eventGraphId);
        predicateRepo.save(predicate);

        return predicate;
    }

    private Concept createConcept(TripleElement element, Map<String, NodeData> nodes, String mappedCollectionId, String eventGraphId) throws NodeNotFoundException {
        Concept concept = new Concept();
        if (element.getType().equals(TripleElement.TYPE_URI)) {
            concept.setLabel(element.getLabel());
            concept.setUri(element.getUri());
            return concept;
        }

        NodeData data = nodes.get(element.getReference());

        if (data == null) {
            throw new NodeNotFoundException("Can't find node with id " + element.getReference());
        }

        concept.setLabel(data.getLabel());
        concept.setUri(data.getMetadata().getInterpretation());
        concept.setMappedCollectionId(mappedCollectionId);
        concept.setLinkedEventGraphId(eventGraphId);
        concept.setMappingType(MappingTypes.DEFAULT_MAPPING);
        return concept;
    }

    private Predicate createPredicate(TripleElement element, Map<String, NodeData> nodes, Concept subject, Concept object, String mappedCollectionId, String eventGraphId) throws NodeNotFoundException {
        Predicate predicate = new Predicate();

        if (element.getType().equals(TripleElement.TYPE_URI)) {
            predicate.setLabel(element.getLabel());
            predicate.setRelationship(element.getUri());
        } else {
            NodeData data = nodes.get(element.getReference());
            if (data == null) {
                throw new NodeNotFoundException("Can't find node with id " + element.getReference());
            }
            predicate.setLabel(data.getLabel());
            predicate.setRelationship(data.getMetadata().getInterpretation());
        }

        predicate.setSource(subject);
        predicate.setTarget(object);
        predicate.setMappedCollectionId(mappedCollectionId);
        predicate.setLinkedEventGraphId(eventGraphId);
        predicate.setMappingType(MappingTypes.DEFAULT_MAPPING);
        return predicate;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.MappedTripleService#getMappedTriples(java.lang.String)
     */
    @Override
    public List<Triple> getMappedTriples(String mappedCollectionId) {
        List<Predicate> predicates = predicateRepo.findByMappedCollectionId(mappedCollectionId);
        return predicates.stream().map(predicate -> toTriple(predicate)).collect(Collectors.toList());
    }

    @Override
    public List<Triple> getTriplesByUri(String mappedCollectionId, String uri) {
        List<Predicate> predicates = predicateRepo.findBySourceUriOrTargetUriAndMappedCollectionId(uri, mappedCollectionId);
        return predicates.stream().map(predicate -> toTriple(predicate)).collect(Collectors.toList());
    }
    
    private Triple toTriple(Predicate predicate) {
        if (predicate == null) {
            return null;
        }

        Triple triple = new Triple();
        triple.setSubject(toTripleElement(predicate.getSource()));
        triple.setObject(toTripleElement(predicate.getTarget()));
        triple.setPredicate(toTripleElement(predicate));
        return triple;
    }

    private TripleElement toTripleElement(Concept concept) {
        TripleElement tripleElement = new TripleElement();
        tripleElement.setId(concept.getId());
        tripleElement.setLabel(concept.getLabel());
        tripleElement.setUri(concept.getUri());
        return tripleElement;
    }

    private TripleElement toTripleElement(Predicate predicate) {
        TripleElement tripleElement = new TripleElement();
        tripleElement.setId(predicate.getId());
        tripleElement.setLabel(predicate.getLabel());
        tripleElement.setUri(predicate.getRelationship());
        return tripleElement;
    }

}
