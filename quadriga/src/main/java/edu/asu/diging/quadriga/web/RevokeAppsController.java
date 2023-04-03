package edu.asu.diging.quadriga.web;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Controller
public class RevokeAppsController {
    

    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/admin/user/{username}/app/{clientId}/revoke", method = RequestMethod.POST)
    public String withdraw(@PathVariable String username, @PathVariable String clientId,@RequestParam("_csrf") String csrfToken,HttpServletRequest request ) {
        simpleUserAppService.delete(username, clientId);
        return "redirect:/admin/user/" + username + "/apps";
    }

}
