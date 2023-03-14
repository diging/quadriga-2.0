package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.conceptpower.data.ConceptCacheRepository;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptService;
import edu.asu.diging.quadriga.core.data.neo4j.ConceptRepository;
import edu.asu.diging.quadriga.core.data.neo4j.PredicateRepository;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.PredicateManager;
import edu.asu.diging.quadriga.web.model.GraphElements;

@Controller
public class ExploreCollectionController {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String URI_PREFIX = "http";

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private MappedTripleService mappedTripleService;
    
    @Autowired
    private ConceptCacheService conceptCacheService;
    
    @Autowired
    private ConceptService conceptService;
  
    @RequestMapping(value = "/auth/collections/{collectionId}/explore")
    public String exploreTriples(@PathVariable String collectionId, Model model) {
        Collection collection;
        try {
            collection = collectionManager.findCollection(collectionId);
        } catch (InvalidObjectIdException e) {
            logger.error("No Collection found for id {}", collectionId);
            return "error404Page";
        }
        model.addAttribute("collectionName", collection.getName());
        model.addAttribute("collection", collectionId);
        return "auth/exploreCollection";
    }

    @GetMapping(value = "/auth/collections/{collectionId}/sub-graph")
    public ResponseEntity<GraphElements> getGraphForUri(@PathVariable String collectionId,
            @RequestParam(value = "uri", required = true) String uri,
            @RequestParam(value = "ignoreList", required = false, defaultValue = "{}") List<String> ignoreList)
            throws InvalidObjectIdException, CollectionNotFoundException {
        
        ConceptCache conceptCache = conceptCacheService.getConceptByUri(uri);
        System.out.println(processUri(conceptCache.getUri()));
        MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndMappingType(collectionId, MappedTripleType.DEFAULT_MAPPING);
        List<DefaultMapping> triples = mappedTripleService.getTriplesByUri(mappedTripleGroup.get_id().toString(),
                mapConceptUriToDatabaseUri(mappedTripleGroup.get_id().toString(),conceptCache.getEqualTo()), ignoreList);
        
        GraphElements graphElements = GraphUtil.mapToGraph(triples);
        return new ResponseEntity<>(graphElements, HttpStatus.OK);
    }

    private String mapConceptUriToDatabaseUri(String mappedTripleGroupId,List<String> equalTo)
    {
        List<Concept> concept = conceptService.findByMappedTripleGroupId(mappedTripleGroupId);
        List<String> conceptUris = new ArrayList<>();
        for(Concept i:concept)
        {
            conceptUris.add(i.getUri());
            System.out.println(i.getUri());
            
        }
        for(String i:equalTo)
        {
            List<String> listOfUris = processUri(i);
            System.out.println(listOfUris);
            listOfUris.retainAll(conceptUris);
            if(!listOfUris.isEmpty())
                return listOfUris.get(0);
        }
        
        return "None"; 
    }
    
    // Normalize the URI prefix and suffix
    private List<String> processUri(String stringBuffer) {
        List<String> listOfUris = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer(stringBuffer);
        if (stringBuffer.startsWith("http"))
        {
            String uri1 =stringBuffer;
            uri1 = uri1.replace("http", "https");
            if (stringBuffer.endsWith("/")) {
                String uri2 = uri1.substring(0,(stringBuffer.length() - 1));
                listOfUris.add(uri2);
            }
            else if (! stringBuffer.endsWith("/")) {
                listOfUris.add(uri1);
            }
        }
        if (!stringBuffer.startsWith(URI_PREFIX)) {
            
            
            if (stringBuffer.endsWith("/")) {
                String uri2 = stringBuffer.substring(0,(stringBuffer.length() - 1));
                listOfUris.add(uri2);
            }
            else if (!stringBuffer.endsWith("/")) {
                listOfUris.add(stringBuffer);
            }
            
        }
        return listOfUris;
    }

}
