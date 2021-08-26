package edu.asu.diging.quadriga.web;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @RequestMapping(value = "/auth/collections/edit", method = RequestMethod.GET)
    public String get(@RequestParam(value = "id", required = true) String id, Model model) {
        Optional<Collection> collection = Optional.ofNullable(collectionManager.findCollection(id));

        if (collection.isPresent()) {
            Collection currentCollection = collection.get();
            CollectionForm collectionForm = new CollectionForm();
            collectionForm.setId(id);
            collectionForm.setName(currentCollection.getName());
            collectionForm.setDescription(currentCollection.getDescription());
            model.addAttribute("collectionForm", collectionForm);

            return "auth/editCollection";
        }

        return "redirect:/auth/collections";
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
    @RequestMapping(value = "/auth/collections/edit", method = RequestMethod.POST)
    public String edit(@RequestParam(value = "id", required = true) String id, @Valid CollectionForm collectionForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("alert_type", "danger");
            redirectAttributes.addFlashAttribute("alert_msg",
                    "Something went wrong while editing collections, please try again!");
            redirectAttributes.addFlashAttribute("show_alert", true);
            return "auth/editCollection";
        }

        Collection collection = null;

        if (Objects.nonNull(collectionForm)) {
            collection = collectionManager.editCollection(id, Optional.ofNullable(collectionForm.getName()),
                    Optional.ofNullable(collectionForm.getDescription()));

            if (Objects.nonNull(collection)) {
                redirectAttributes.addFlashAttribute("alert_type", "success");
                redirectAttributes.addFlashAttribute("alert_msg", "Collection has been edited.");
                redirectAttributes.addFlashAttribute("show_alert", true);
                return "redirect:/auth/collections";
            }
        }

        redirectAttributes.addFlashAttribute("alert_type", "danger");
        redirectAttributes.addFlashAttribute("alert_msg",
                "Something went wrong while editing collections, please try again!");
        redirectAttributes.addFlashAttribute("show_alert", true);
        return "auth/editCollection";

    }

}
