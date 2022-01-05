package edu.asu.diging.quadriga.web;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.MappingTypes;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.ConceptManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;
    
    @Autowired
    private ConceptManager conceptManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        
        Collection collection;
        try {
            collection = collectionManager.findCollection(id);
            if(collection == null) {
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            return "error404Page";
        }
        
        ObjectId collectionId = collection.getId();
        
        List<EventGraph> eventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collectionId);
        
        if(!eventGraphs.isEmpty()) {
            // We get last network submission info by getting the last EventGraph which will be a part of the last network
            EventGraph lastNetwork = eventGraphs.get(0);
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime());
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getAppName());
        }
        
        model.addAttribute("collection", collection);
        
        // One network may have multiple eventGraphs, but all of them will have same sourceURI in the context
        // This sourceURI will be used to group eventGraphs together that belong to the same network
        model.addAttribute("numberOfSubmittedNetworks", eventGraphs.stream().collect(Collectors.groupingBy(event -> event.getContext().getSourceUri())).size());
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", getDefaultMappingss(collectionId.toString()));
        
        return "auth/displayCollection";
        
    }
    
    /**
     * This method gets a mappedTripleGroup using the current collectionId
     * Once a mappedTripleGroup is found, it looks for this MappedTripleGroup in 'Concept' nodes
     * A count of all such concepts with mapping type "DefaultMapping" are returned
     * 
     * Before returning, we divide the count of concepts by two because every default
     * mapping has two concepts, a subject and an object
     * 
     * @param collectionId used to find mappedTripleGroupId
     * @return the number of default mappings
     */
    private int getDefaultMappingss(String collectionId) {
        int defaultMappings = 0;
        MappedTripleGroup mappedTripleGroup = null;
        try {
            mappedTripleGroup = mappedTripleGroupService.findMappedTripleGroupByCollectionId(collectionId.toString());
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't find mappedTripleGroup to get default mappings from Neo4J for collectionId: " + collectionId);
        }
        
        if(mappedTripleGroup != null) {
            List<Concept> concepts = conceptManager.findConceptsByMappingTypeAndMappedTripleGroupId(MappingTypes.DEFAULT_MAPPING, mappedTripleGroup.get_id().toString());
            // We need to divide by two as one default mapping will have 2 concepts, a subject and an object
            defaultMappings = (concepts.isEmpty() ? 0 : concepts.size()) / 2;
        } else {
            logger.error("Couldn't find mappedTripleGroup to get default mappings from Neo4J for collectionId: " + collectionId);
        }
        return defaultMappings;
    }

}
