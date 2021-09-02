package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.service.CollectionManager;

@Controller
public class DeleteCollectionController {
    
    @Autowired
    private CollectionManager collectionManager;
    
    @RequestMapping(value = "/auth/collections/delete", method = RequestMethod.GET)
    public String get(@RequestParam(value = "id", required = true) String id, RedirectAttributes redirectAttributes) {
        
        boolean collectionDeleted = collectionManager.deleteCollection(id);
        
        if(collectionDeleted) {
            redirectAttributes.addFlashAttribute("alert_type", "success");
            redirectAttributes.addFlashAttribute("alert_msg", "Collection has been deleted.");
            redirectAttributes.addFlashAttribute("show_alert", true);
        } else {
            redirectAttributes.addFlashAttribute("alert_type", "danger");
            redirectAttributes.addFlashAttribute("alert_msg", "Something went wrong while deleting collection, please try again!");
            redirectAttributes.addFlashAttribute("show_alert", true);
        }
        
        return "redirect:/auth/collections";
    }
    
}
