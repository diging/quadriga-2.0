package edu.asu.diging.quadriga.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
        Page<EventGraph> eventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collection.getId(), PageRequest.of(page, size));
        Map<ObjectId, Integer> triplesMap =  new HashMap<>();
        
        if(!eventGraphs.isEmpty()) {
            EventGraph lastNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime());
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getAppName());
            for(EventGraph eventGraph: eventGraphs) {
                int numberOfTriples = 0;
                try {
                    numberOfTriples = eventGraphService.findAllTriplesInEventGraph(eventGraph.getId());
                } catch (TriplesNotFoundException e) {
                    logger.error(e.getMessage());
                }
                triplesMap.put(eventGraph.getId(), numberOfTriples);
            }
        }
        
        model.addAttribute("collection", collection);
        model.addAttribute("numberOfSubmittedNetworks", eventGraphs.getTotalElements());
        model.addAttribute("networks", eventGraphs);
        model.addAttribute("triplesMap", triplesMap);
        model.addAttribute("size", size);
        
        return "auth/displayCollection";
        
    }

}
