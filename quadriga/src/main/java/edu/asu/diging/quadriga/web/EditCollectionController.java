package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.quadriga.web.forms.CollectionForm;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

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
    
    @Autowired
    private CitesphereConnector citesphereConnector;
    
    @Autowired
    private SimpleUserAppService simpleUserAppService;

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
        Collection collection = collectionManager.findCollection(id);

        if (Objects.nonNull(collection)) {
            CollectionForm collectionForm = new CollectionForm();
            collectionForm.setId(id);
            collectionForm.setName(collection.getName());
            collectionForm.setDescription(collection.getDescription());
            collectionForm.setApps(collection.getApps());
            
            SimpleUser user = (SimpleUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	
        	// Get the set of apps that are accessible by the user
            Set<String> userAppClientIds = simpleUserAppService.findByUsername(user.getUsername())
            		.stream()
            		.map(userApp -> userApp.getAppClientId())
            		.collect(Collectors.toSet());
            
            // Get all apps from citesphere and filter them to only display apps accessible by the user
            List<CitesphereAppInfo> apps = new ArrayList<>();
            
            if(!userAppClientIds.isEmpty()) {
    	        apps = citesphereConnector.getCitesphereApps()
    	        		.stream()
    	        		.filter(app -> userAppClientIds.contains(app.getClientId()))
    	        		.collect(Collectors.toList());
            }
            
            model.addAttribute("citesphereApps", apps);
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
        } catch (CollectionNotFoundException e) {
            return "error404Page";
        }
       

    }

}
