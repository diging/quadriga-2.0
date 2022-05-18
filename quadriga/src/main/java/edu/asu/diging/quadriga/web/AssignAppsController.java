package edu.asu.diging.quadriga.web;

import java.util.HashSet;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.UserAppNotFoundException;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class AssignAppsController {

    @Autowired
    private SimpleUserAppService simpleUserAppService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/admin/user/{username}/apps", method = RequestMethod.GET)
    public String get(@PathVariable String username, Model model) {

        HashSet<String> userApps = new HashSet<>();
        simpleUserAppService.findByUsername(username).forEach(userApp -> userApps.add(userApp.getAppClientId()));

        model.addAttribute("username", username);
        model.addAttribute("userApps", userApps);

        return "admin/user/apps";
    }

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/assign", method = RequestMethod.GET)
    public String assign(@PathVariable String username, @PathVariable String clientId) {

        SimpleUserApp simpleUserApp = new SimpleUserApp();

        simpleUserApp.setId(UUID.randomUUID().toString());
        simpleUserApp.setUsername(username);
        simpleUserApp.setAppClientId(clientId);

        simpleUserAppService.save(simpleUserApp);

        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/revoke", method = RequestMethod.GET)
    public String withdraw(@PathVariable String username, @PathVariable String clientId) {

        try {
            simpleUserAppService.delete(username, clientId);
        } catch (UserAppNotFoundException e) {
            logger.error("Couldn't find app " + clientId + " assigned to user " + username, e);
        }

        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }

}
