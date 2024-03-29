package edu.asu.diging.quadriga.web;

import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.exceptions.CitesphereAppNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

/**
 * This controller enables users to edit their collections
 * 
 * @author poojakulkarni
 *
 */
@Controller
public class EditCollectionController {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionManager collectionManager;

    /**
     * Request handler for getting the "Edit collections" view
     * 
     * @param id    of the collection to be edited
     * @param model to set collectionForm attribute to pre-fill current name and
     *              description
     * @return the editCollection view
     */
    @RequestMapping(value = "/auth/collections/{id}/edit", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        Collection collection = null;

        try {
            collection = collectionManager.findCollection(id);
            if (Objects.nonNull(collection)) {
                CollectionForm collectionForm = new CollectionForm();
                collectionForm.setId(id);
                collectionForm.setName(collection.getName());
                collectionForm.setDescription(collection.getDescription());
                collectionForm.setApps(collection.getApps());
                model.addAttribute("collectionForm", collectionForm);
                return "auth/editCollection";
            } else {
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            logger.error("Couldn't edit collection", e);
            return "error404Page";
        }
    }

    /**
     * Request handler for editing a collection in the "Edit collections" view
     * 
     * @param id                 of the collection to be edited
     * @param collectionForm     that has the updated name and description values
     * @param bindingResult      for checking form errors
     * @param redirectAttributes used for adding flash attributes after redirecting
     * @return
     */
    @RequestMapping(value = "/auth/collections/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable String id, @Valid CollectionForm collectionForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/editCollection";
        }

        try {
            collectionManager.editCollection(id, collectionForm.getName(), collectionForm.getDescription(), collectionForm.getApps());
            
            redirectAttributes.addFlashAttribute("alert_type", "success");
            redirectAttributes.addFlashAttribute("alert_msg", "Collection has been edited.");
            redirectAttributes.addFlashAttribute("show_alert", true);
            return "redirect:/auth/collections";
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't edit collection", e);
            return "error404Page";
        } catch (CitesphereAppNotFoundException e) {
            logger.error("Couldn't edit collection", e);
            bindingResult.rejectValue("apps", "error.collectionForm", e.getMessage());
            return "auth/editCollection";
        }
    }
}
