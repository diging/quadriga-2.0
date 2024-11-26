package edu.asu.diging.quadriga.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class AssignAppsController {
    
    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/assign", method = RequestMethod.POST)
    public String assign(@PathVariable String username, @PathVariable String clientId, HttpServletRequest request){
        simpleUserAppService.save(username, clientId);
        return "redirect:/admin/user/" + username + "/apps";
    }
    
}