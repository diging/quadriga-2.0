package edu.asu.diging.quadriga.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionManager collectionManager;

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        return "auth/addCollection";
    }

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.POST)
    public String add(@Valid CollectionForm collectionForm, BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "auth/addCollection";
        }
        
        try {
            collectionManager.addCollection(collectionForm.getName(), collectionForm.getDescription(),
                    collectionForm.getApps());
        } catch (CitesphereAppNotFoundException e) {
            result.rejectValue("apps", "error.collectionForm", e.getMessage());
            logger.error("Couldn't add collection",e);
            return "auth/addCollection";
        }

        redirectAttrs.addFlashAttribute("show_alert", true);
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Collection has been added.");
        return "redirect:/auth/collections";
    }

}
