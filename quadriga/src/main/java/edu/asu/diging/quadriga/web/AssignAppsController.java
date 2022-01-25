package edu.asu.diging.quadriga.web;

import java.util.HashSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class AssignAppsController {
    
    @Autowired
    private CitesphereConnector citesphereConnector;
    
    @Autowired
    private SimpleUserAppService simpleUserAppService;
    
    @RequestMapping(value = "/admin/user/{username}/apps", method = RequestMethod.GET)
    public String get(@PathVariable String username, Model model) {
        
        HashSet<String> userApps = new HashSet<>();
        simpleUserAppService.findByUsername(username).forEach(userApp -> userApps.add(userApp.getAppClientId()));
        
        System.out.println(userApps);
        
        model.addAttribute("username", username);
        model.addAttribute("apps", citesphereConnector.getCitesphereApps());
        model.addAttribute("userApps", userApps);
        
        return "admin/user/apps";
    }
    
    @RequestMapping(value = "/admin/user/{username}/apps/assign/{clientId}", method = RequestMethod.GET)
    public String assignApps(@PathVariable String username, @PathVariable String clientId) {
        
        SimpleUserApp simpleUserApp = new SimpleUserApp();
        
        simpleUserApp.setId(UUID.randomUUID().toString());
        simpleUserApp.setUsername(username);
        simpleUserApp.setAppClientId(clientId);
        
        simpleUserAppService.save(simpleUserApp);
        
        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }
    
}
