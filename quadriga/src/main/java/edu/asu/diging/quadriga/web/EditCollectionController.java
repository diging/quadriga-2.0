package edu.asu.diging.quadriga.web;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
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
    @RequestMapping(value = "/auth/collections/edit/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        Collection collection = collectionManager.findCollection(id);

        if (Objects.nonNull(collection)) {
            CollectionForm collectionForm = new CollectionForm();
            collectionForm.setId(id);
            collectionForm.setName(collection.getName());
            collectionForm.setDescription(collection.getDescription());
            model.addAttribute("collectionForm", collectionForm);

            return "auth/editCollection";
        } else {
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
    @RequestMapping(value = "/auth/collections/edit/{id}", method = RequestMethod.POST)
    public String edit(@PathVariable String id, @Valid CollectionForm collectionForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/editCollection";
        }

        try {
            collectionManager.editCollection(id, collectionForm.getName(), collectionForm.getDescription());
            
            redirectAttributes.addFlashAttribute("alert_type", "success");
            redirectAttributes.addFlashAttribute("alert_msg", "Collection has been edited.");
            redirectAttributes.addFlashAttribute("show_alert", true);
            return "redirect:/auth/collections";
        } catch (CollectionNotFoundException e) {
            return "error404Page";
        }
       

    }

}
