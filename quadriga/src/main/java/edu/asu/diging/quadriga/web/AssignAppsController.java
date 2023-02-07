package edu.asu.diging.quadriga.web;

import java.util.HashSet;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class AssignAppsController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/admin/user/{username}/apps", method = RequestMethod.GET)
    public String get(@PathVariable String username, Model model,HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        HashSet<String> userApps = new HashSet<>();
        simpleUserAppService.findByUsername(username).forEach(userApp -> userApps.add(userApp.getAppClientId()));

        model.addAttribute("username", username);
        model.addAttribute("userApps", userApps);
        model.addAttribute("csrfToken",csrfToken);
        System.out.println(csrfToken);
        return "admin/user/apps";
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/assign", method = RequestMethod.POST)
    public String assign(@PathVariable String username, @PathVariable String clientId) {
        
        simpleUserAppService.save(username, clientId);
        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/revoke", method = RequestMethod.POST)
    public String withdraw(@PathVariable String username, @PathVariable String clientId) {
        
        simpleUserAppService.delete(username, clientId);
        return "redirect: /quadriga/admin/user/" + username + "/apps";
    }

}
