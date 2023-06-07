package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;

import java.util.List;


import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.MappedTriplesPage;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.core.conceptpower.data.ConceptCacheRepository;
import edu.asu.diging.quadriga.core.conceptpower.model.CachedConcept;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Service
public class MappedTripleServiceImpl implements MappedTripleService {
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String URI_PREFIX = "http";
    private static final String URI_PREFIX_1 = "https";

    @Autowired
    private ConceptRepository conceptRepo;

    @Autowired
    private PredicateRepository predicateRepo;
    
    @Autowired
    private ConceptCacheService conceptCacheService;
    
    @Autowired
    private ConceptCacheRepository conceptCacheRepo;
    
   
    
    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.MappedTripleService#storeMappedGraph(edu.asu.diging.quadriga.api.v1.model.Graph, edu.asu.diging.quadriga.core.model.MappedTripleGroup)
     */
    @Override
    public Predicate storeMappedGraph(Graph graph, MappedTripleGroup mappedTripleGroup) throws NodeNotFoundException {
        DefaultMapping mapping = graph.getMetadata().getDefaultMapping();
        if (mapping == null) {
            return null;
        }

        String mappedTripleGroupId = mappedTripleGroup.get_id().toString();
        Map<String, NodeData> nodes = graph.getNodes();
        Concept subject = createConcept(mapping.getSubject(), nodes, mappedTripleGroupId);
        subject = conceptRepo.save(subject);
        Concept object = createConcept(mapping.getObject(), nodes, mappedTripleGroupId);
        object = conceptRepo.save(object);
        Predicate predicate = createPredicate(mapping.getPredicate(), nodes, subject, object, mappedTripleGroupId);
        predicateRepo.save(predicate);

        return predicate;
    }

    private Concept createConcept(TripleElement element, Map<String, NodeData> nodes, String mappedTripleGroupId) throws NodeNotFoundException {
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
        return concept;
    }

    private Predicate createPredicate(TripleElement element, Map<String, NodeData> nodes, Concept subject, Concept object, String mappedTripleGroupId) throws NodeNotFoundException {
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
        predicate.setMappedTripleGroupId(mappedTripleGroupId);
        return predicate;
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.MappedTripleService#getMappedTriples(java.lang.String, int, int)
     */
    @Override
    public MappedTriplesPage getMappedTriples(String mappedTripleGroupId, int page, int pageSize) {
        MappedTriplesPage triplePage = new MappedTriplesPage();
        Optional<Page<Predicate>> predicates = predicateRepo.findByMappedTripleGroupId(mappedTripleGroupId, PageRequest.of(page-1, pageSize));
        if (predicates.isPresent()) {
            triplePage.setTriples(predicates.get().stream().map(predicate -> toTriple(predicate)).collect(Collectors.toList()));
            triplePage.setCurrentPage(predicates.get().getNumber() + 1);
            triplePage.setTotalPages(predicates.get().getTotalPages());
        }
        return triplePage;
    }


    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.MappedTripleService#getTriplesByUri(java.lang.String, java.lang.String, java.util.List)
     */
    @Override
    public List<DefaultMapping> getTriplesByUri(String mappedTripleGroupId, String uri, List<String> ignoreList){
        
        CachedConcept conceptCache = conceptCacheService.getConceptByUri(uri);
        List<Predicate> predicates = predicateRepo.findBySourceUriOrTargetUriAndMappedTripleGroupId(mapConceptUriToDatabaseURI(mappedTripleGroupId,conceptCache.getEqualTo()),ignoreList,mappedTripleGroupId);
        return predicates.stream().map(predicate -> toTriple(predicate)).collect(Collectors.toList());
    }    

    private DefaultMapping toTriple(Predicate predicate) {
        if (predicate == null) {
            return null;
        }

        DefaultMapping triple = new DefaultMapping();
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
    
    private String mapConceptUriToDatabaseURI(String mappedTripleGroupId,List<String> equalTo){
        List<Concept> concepts = conceptRepo.findByMappedTripleGroupId(mappedTripleGroupId);
        List<String> conceptUris = new ArrayList<>();
      
        for(Concept eachConcept:concepts){
            conceptUris.add(eachConcept.getUri());
        }
        for(String similarUri:equalTo){
            List<String> listOfUris = processUri(similarUri);
            listOfUris.retainAll(conceptUris);
            if(!listOfUris.isEmpty()) {
                return listOfUris.get(0);
            }
        }
        return null;
             
    }
    
    /**
     * @param uri is the uri that has to be normalized with two prefixes and a trailing slash
     * @return a list of strings which are the uri normalised with the prefixes and trailing slash
     */
    private List<String> processUri(String uri) {
        List<String> listOfUris = new ArrayList<>();
        String regex = "https?(://.*?)/?$";
        
        Pattern pattern = Pattern.compile(regex);
        
        Matcher matcher = pattern.matcher(uri);
        
        if(matcher.find()) {
            String extractedText = matcher.group(1);
            
            listOfUris.add(URI_PREFIX + extractedText);
            listOfUris.add(URI_PREFIX + extractedText+"/");
            listOfUris.add(URI_PREFIX_1 + extractedText);
            listOfUris.add(URI_PREFIX_1 + extractedText+"/");
        }
        return listOfUris;
    }

}
