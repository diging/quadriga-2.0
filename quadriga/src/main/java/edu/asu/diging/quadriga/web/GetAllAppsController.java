package edu.asu.diging.quadriga.web;

import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class GetAllAppsController {
    
    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/admin/user/{username}/apps", method = RequestMethod.GET)
    public String get(@PathVariable String username, Model model,HttpServletRequest request) {
        HashSet<String> userApps = new HashSet<>();
        simpleUserAppService.findByUsername(username).forEach(userApp -> userApps.add(userApp.getAppClientId()));
        model.addAttribute("username", username);
        model.addAttribute("userApps", userApps);
        return "admin/user/apps";
    }
    
}
