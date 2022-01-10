package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private CitesphereConnector citesphereConnector;

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        return "auth/addCollection";
    }

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.POST)
    public String add(Model model, @Valid CollectionForm collectionForm, BindingResult result, RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "auth/addCollection";
        }
        
        List<CitesphereAppInfo> citesphereApps = citesphereConnector.getCitesphereApps();
        HashSet<String> appSet = new HashSet<String>(
                citesphereApps.stream().map(app -> app.getClientId()).collect(Collectors.toList()));
        List<String> invalidApps = new ArrayList<>();
        collectionForm.getApps().forEach(clientId -> {
            if (!appSet.contains(clientId)) {
                invalidApps.add(clientId);
            }
        });
        
        if (!invalidApps.isEmpty()) {
            model.addAttribute("collectionForm", collectionForm);
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
