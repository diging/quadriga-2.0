package edu.asu.diging.quadriga.web;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.MappingTypes;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.ConceptManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private MappedCollectionService mappedCollectionService;
    
    @Autowired
    private ConceptManager conceptManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{collectionId}", method = RequestMethod.GET)
    public String get(HttpServletRequest request, @PathVariable String collectionId, Model model) {
        
        Collection collection;
        try {
            collection = collectionManager.findCollection(collectionId);
            if(collection == null) {
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            return "error404Page";
        }
        
        int page = 0;
        int size = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
            page = page < 0 ? 0 : page;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
            size = size < 1 ? 10 : size;
        }
        
        Page<EventGraph> pagedEventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collection.getId(), PageRequest.of(page, size));
        Page<EventGraph> unpagedEventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collection.getId(), Pageable.unpaged());
        int defaultMappings = 0;
        
        if(!pagedEventGraphs.isEmpty()) {
            EventGraph lastNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime());
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getAppName());
            for(EventGraph eventGraph: unpagedEventGraphs.getContent()) {
                if(eventGraph.getDefaultMapping() != null) {
                    defaultMappings++;
                }
            }
        }
        
        model.addAttribute("collection", collection);
        model.addAttribute("numberOfSubmittedNetworks", pagedEventGraphs.getTotalElements());
        model.addAttribute("networks", pagedEventGraphs);
        model.addAttribute("size", size);
        model.addAttribute("defaultMappings", defaultMappings);
        model.addAttribute("collection", collection);
        
        // One network may have multiple eventGraphs, but all of them will have same sourceURI in the context
        // This sourceURI will be used to group eventGraphs together that belong to the same network
        model.addAttribute("numberOfSubmittedNetworks", unpagedEventGraphs.stream().collect(Collectors.groupingBy(event -> event.getContext().getSourceUri())).size());
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", getDefaultMappingss(collectionId.toString()));
        
        return "auth/displayCollection";
        
    }
    
    /**
     * This method gets a mappedCollection using the current collectionId
     * Once a mappedCollection is found, it looks for this MappedCollection in 'Concept' nodes
     * A count of all such concepts with mapping type "DefaultMapping" are returned
     * 
     * Before returning, we divide the count of concepts by two because every default
     * mapping has two concepts, a subject and an object
     * 
     * @param collectionId used to find mappedCollectionId
     * @return the number of default mappings
     */
    private int getDefaultMappingss(String collectionId) {
        int defaultMappings = 0;
        MappedCollection mappedCollection = null;
        try {
            mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId.toString());
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't find mappedCollection to get default mappings from Neo4J for collectionId: " + collectionId);
        }
        
        if(mappedCollection != null) {
            List<Concept> concepts = conceptManager.findConceptsByMappingTypeAndMappedCollectionId(MappingTypes.DEFAULT_MAPPING, mappedCollection.get_id().toString());
            // We need to divide by two as one default mapping will have 2 concepts, a subject and an object
            defaultMappings = (concepts.isEmpty() ? 0 : concepts.size()) / 2;
        } else {
            logger.error("Couldn't find mappedCollection to get default mappings from Neo4J for collectionId: " + collectionId);
        }
        return defaultMappings;
    }

}
