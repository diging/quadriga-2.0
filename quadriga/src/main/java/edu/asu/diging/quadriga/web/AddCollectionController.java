package edu.asu.diging.quadriga.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.citesphere.ICitesphereConnector;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private ICitesphereConnector citesphereConnector;

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        model.addAttribute("citesphereApps", citesphereConnector.getCitesphereApps());
        return "auth/addCollection";
    }

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.POST)
    public String add(@Valid CollectionForm collectionForm, BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "auth/addCollection";
        }
        collectionManager.addCollection(collectionForm.getName(), collectionForm.getDescription(),
                collectionForm.getApps());

        redirectAttrs.addFlashAttribute("show_alert", true);
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Collection has been added.");
        return "redirect:/auth/collections";
    }

}
