package edu.asu.diging.quadriga.web;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.ZoneId;
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
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.PredicateManager;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;
    
    @Autowired
    private PredicateManager predicateManager;
    
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{collectionId}", method = RequestMethod.GET)
    public String get(HttpServletRequest request, @PathVariable String collectionId, Model model) {

        // Get collection details
        Collection collection;
        try {
            collection = collectionManager.findCollection(collectionId);
            if(collection == null) {
            	logger.error("Couldn't find collection: ", collectionId);
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            logger.error("Couldn't find collection ", e);
            return "error404Page";
        }

        model.addAttribute("collection", collection);

        // Determine page number and size for network pagination
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

        model.addAttribute("size", size);

        EventGraph latestNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
        
        model.addAttribute("latestNetwork", latestNetwork);

        // Get all EventGraphs for this collection
        List<EventGraph> eventGraphsList = eventGraphService.findAllEventGraphsByCollectionId(collection.getId());


        long numberOfSubmittedNetworks = eventGraphService.getNumberOfSubmittedNetworks(collection.getId());


        model.addAttribute("networks", eventGraphsList.subList(page * size, Math.min(eventGraphsList.size(), page * size + size)));

        model.addAttribute("totalPages", eventGraphsList.size() % 10 == 0 ? (eventGraphsList.size()/size) : (eventGraphsList.size()/size + 1));
        model.addAttribute("pageNumber", page);
        model.addAttribute("numberOfSubmittedNetworks", numberOfSubmittedNetworks);
        model.addAttribute("collection", collection);
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", collectionManager.getNumberOfDefaultMappings(collection.getId().toString()));
        
        return "auth/displayCollection";       
    }
}
