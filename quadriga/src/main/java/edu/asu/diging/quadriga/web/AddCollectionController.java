package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.quadriga.web.forms.CollectionForm;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

@Controller
public class AddCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private CitesphereConnector citesphereConnector;
    
    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
    	
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
        
        
        model.addAttribute("collectionForm", new CollectionForm());
        model.addAttribute("citesphereApps", apps);
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
