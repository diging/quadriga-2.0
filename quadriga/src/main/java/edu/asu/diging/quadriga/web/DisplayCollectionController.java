package edu.asu.diging.quadriga.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    
    
    @RequestMapping(value = "/auth/collections/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        Collection collection = collectionManager.findCollection(id);
        if(collection == null) {
            return "error404Page";
        }
        List<EventGraph> eventGraphs = eventGraphService.findAllEventGraphsByCollectionId(id);
        
        model.addAttribute("collection", collection);
        model.addAttribute("networks", eventGraphs);
        return "auth/displayCollection";
    }
    

}
