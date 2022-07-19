package edu.asu.diging.quadriga.web;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class AssignAppsController {

    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/admin/user/{username}/apps", method = RequestMethod.GET)
    public String get(@PathVariable String username, Model model) {

        HashSet<String> userApps = new HashSet<>();
        simpleUserAppService.findByUsername(username).forEach(userApp -> userApps.add(userApp.getAppClientId()));

        model.addAttribute("username", username);
        model.addAttribute("userApps", userApps);

        return "admin/user/apps";
    }

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/assign", method = RequestMethod.POST)
    public String assign(@PathVariable String username, @PathVariable String clientId) {
        simpleUserAppService.save(username, clientId);
        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/revoke", method = RequestMethod.POST)
    public String withdraw(@PathVariable String username, @PathVariable String clientId) {
        simpleUserAppService.delete(username, clientId);
        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }

}
