package edu.asu.diging.quadriga.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.web.service.GraphCreationService;

@Controller
public class NetworkController {
    
    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private GraphCreationService graphCreationService;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @RequestMapping(value = "/auth/collections/{collectionId}/network/{networkId}")
    public String get(@PathVariable String collectionId, @PathVariable String networkId, Model model) {
        
        EventGraph eventGraph;
        try {
            eventGraph = eventGraphService.findEventGraphById(networkId);
        } catch (InvalidObjectIdException e) {
            logger.error(e.getMessage());
            return "error404Page";
        }
        
        if(eventGraph == null) {
            logger.error("No eventGraph found for eventGraphId: " + networkId);
            return "error404Page";
        }
        
        model.addAttribute("elements", graphCreationService.createGraph(eventGraph));
        return "auth/displayNetwork";
    }
    
}