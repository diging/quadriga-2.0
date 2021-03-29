package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.service.ICollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {


    @Autowired
    private ICollectionManager collectionManager;

    @RequestMapping(value = "/admin/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collection", new CollectionForm());
        return "admin/addCollection";
    }

    @RequestMapping(value = "/admin/collections/add", method = RequestMethod.POST)
    public String add(@ModelAttribute CollectionForm collectionForm, RedirectAttributes redirectAttrs) {

        collectionManager.addCollection(collectionForm);
        
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Collection has been added.");
        return "redirect:/admin/collections/add";
    }
    
}
