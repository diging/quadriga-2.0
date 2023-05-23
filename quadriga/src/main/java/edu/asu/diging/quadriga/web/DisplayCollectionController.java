package edu.asu.diging.quadriga.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
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
    
    //@Value("${defaultPageSize}")
    private Integer defaultPageSize=10;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{collectionId}", method = RequestMethod.GET)
    public String get( @RequestParam(required = false) Integer page , @RequestParam(required = false) Integer  size, @PathVariable String collectionId, Model model) {

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

//      Determine page number and size for network pagination      
        page = (page == null || page < 0) ? 0 : page - 1;
        size = (size == null || size < 1) ? defaultPageSize : size;
        

        model.addAttribute("size", size);

        EventGraph latestNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
        
        model.addAttribute("latestNetwork", latestNetwork);
        
        Pageable paging = PageRequest.of(page, size);

        // Get all EventGraphs for this collection
        Page<EventGraph> eventGraphsList = eventGraphService.findAllEventGraphsByCollectionId(collection.getId(), paging);


        long numberOfSubmittedNetworks = eventGraphService.getNumberOfSubmittedNetworks(collection.getId());


        model.addAttribute("networks", eventGraphsList.getContent());
        model.addAttribute("totalPages", eventGraphsList.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("numberOfSubmittedNetworks", numberOfSubmittedNetworks);
        model.addAttribute("collection", collection);
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", collectionManager.getNumberOfDefaultMappings(collection.getId().toString()));
        
        return "auth/displayCollection";       
    }
}
