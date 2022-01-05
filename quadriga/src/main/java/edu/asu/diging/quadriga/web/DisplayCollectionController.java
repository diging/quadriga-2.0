package edu.asu.diging.quadriga.web;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
import edu.asu.diging.quadriga.core.service.CollectionManager;
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
        model.addAttribute("defaultMappings", getDefaultMappings(collectionId.toString()));
        
        return "auth/displayCollection";
        
    }
    
    /**
     * This method gets all mappedTripleGroups that have the current collectionId for which
     * the mappedTripleType is "DefaultMapping"
     * 
     * @param collectionId used to find mappedTripleGroupId
     * @return the number of default mappings
     */
    private int getDefaultMappings(String collectionId) {
        try {
            List<MappedTripleGroup> defaultMappings = mappedTripleGroupService.findDefaultMappedTripleGroupsByCollectionId(collectionId.toString());
            if(defaultMappings == null || defaultMappings.isEmpty()) return 0;
            return defaultMappings.size();
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

}
