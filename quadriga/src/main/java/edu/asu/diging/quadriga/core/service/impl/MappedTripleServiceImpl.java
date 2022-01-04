package edu.asu.diging.quadriga.core.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
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

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.impl.MappedTripleService#storeMappedGraph(edu.asu.diging.quadriga.api.v1.model.Graph)
     */
    @Override
    public Predicate storeMappedGraph(Graph graph, MappedTripleGroup mappedTripleGroup, String eventGraphId) throws NodeNotFoundException {
        DefaultMapping mapping = graph.getMetadata().getDefaultMapping();
        if (mapping == null) {
            return null;
        }

        String mappedTripleGroupId = mappedTripleGroup.get_id().toString();
        Map<String, NodeData> nodes = graph.getNodes();
        Concept subject = createConcept(mapping.getSubject(), nodes, mappedTripleGroupId, eventGraphId);
        subject = conceptRepo.save(subject);
        Concept object = createConcept(mapping.getObject(), nodes, mappedTripleGroupId, eventGraphId);
        object = conceptRepo.save(object);
        Predicate predicate = createPredicate(mapping.getPredicate(), nodes, subject, object, mappedTripleGroupId, eventGraphId);
        predicateRepo.save(predicate);
        
        return predicate;
    }

    private Concept createConcept(TripleElement element, Map<String, NodeData> nodes, String mappedTripleGroupId, String eventGraphId) throws NodeNotFoundException {
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
        concept.setMappedTripleGroupId(mappedTripleGroupId);
        concept.setLinkedEventGraphId(eventGraphId);
        concept.setMappingType(MappingTypes.DEFAULT_MAPPING);
        return concept;
    }

    private Predicate createPredicate(TripleElement element, Map<String, NodeData> nodes, Concept subject, Concept object, String mappedTripleGroupId, String eventGraphId) throws NodeNotFoundException {
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
        predicate.setLinkedEventGraphId(eventGraphId);
        predicate.setMappingType(MappingTypes.DEFAULT_MAPPING);
        predicate.setMappedTripleGroupId(mappedTripleGroupId);
        return predicate;
    }
}
