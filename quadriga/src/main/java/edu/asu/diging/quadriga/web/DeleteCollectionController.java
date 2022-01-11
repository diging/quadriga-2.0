package edu.asu.diging.quadriga.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Controller
public class DeleteCollectionController {
    
    @Autowired
    private CollectionManager collectionManager;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @RequestMapping(value = "/auth/collections/{id}/delete", method = RequestMethod.GET)
    public String get(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            collectionManager.deleteCollection(id);
            redirectAttributes.addFlashAttribute("alert_type", "success");
            redirectAttributes.addFlashAttribute("alert_msg", "Collection has been deleted.");
            redirectAttributes.addFlashAttribute("show_alert", true);
        
            return "redirect:/auth/collections";
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't delete collection", e);
            return "error404Page";
        }
    }
    
}
