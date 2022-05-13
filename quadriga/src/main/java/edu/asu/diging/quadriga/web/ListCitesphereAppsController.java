package edu.asu.diging.quadriga.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

@Controller
public class ListCitesphereAppsController {

    @Autowired
    private SimpleUserAppService simpleUserAppService;

    @GetMapping("/citesphere/apps")
    public ResponseEntity<List<CitesphereAppInfo>> getCitesphereApps(Authentication authentication) {
        SimpleUser user = (SimpleUser) authentication.getPrincipal();
        return new ResponseEntity<>(simpleUserAppService.getAccessibleCitesphereApps(user), HttpStatus.OK);
    }
}
