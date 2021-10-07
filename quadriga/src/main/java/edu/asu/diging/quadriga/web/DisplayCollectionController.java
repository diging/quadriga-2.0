package edu.asu.diging.quadriga.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.TriplesNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Controller
public class DisplayCollectionController {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;

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
        
        List<EventGraph> eventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collection.getId());
        Map<ObjectId, Integer> triplesMap =  new HashMap<>(); 
        
        if(!eventGraphs.isEmpty()) {
            EventGraph lastNetwork = eventGraphs.get(0);
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime());
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getAppName());
            for(EventGraph eventGraph: eventGraphs) {
                int numberOfTriples = 0;
                try {
                    numberOfTriples = eventGraphService.findAllTriplesInEventGraph(eventGraph.getId());
                } catch (TriplesNotFoundException e) {
                    logger.error("Could not find triples for network with EventGraph ID: " + eventGraph.getId().toString());
                }
                triplesMap.put(eventGraph.getId(), numberOfTriples);
            }
        }
        
        model.addAttribute("collection", collection);
        model.addAttribute("numberOfSubmittedNetworks", eventGraphs.size());
        model.addAttribute("networks", eventGraphs);
        model.addAttribute("triplesMap", triplesMap);
        
        return "auth/displayCollection";
        
    }

}
