package edu.asu.diging.quadriga.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;
    
   
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        
        Collection collection;
        try {
            collection = collectionManager.findCollection(id);
            if(collection == null) {
            	logger.error("Couldn't find collection: ", id);
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            logger.error("Couldn't find collection ", e);
            return "error404Page";
        }
        
       
        
        EventGraph latestNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
        
        model.addAttribute("latestNetwork", latestNetwork);

        model.addAttribute("collection", collection);
        
        long numberOfSubmittedNetworks = eventGraphService.getNumberOfSubmittedNetworks(collection.getId());
       
        model.addAttribute("numberOfSubmittedNetworks", numberOfSubmittedNetworks);
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", collectionManager.getNumberOfDefaultMappings(collection.getId().toString()));
        
        return "auth/displayCollection";
        
    }

}
