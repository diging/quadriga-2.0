package edu.asu.diging.quadriga.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

@Controller
public class ListCitesphereAppsController {

    @Autowired
    private CitesphereConnector citesphereConnector;

    @GetMapping("/citesphere/apps")
    public ResponseEntity<List<CitesphereAppInfo>> getCitesphereApps() {
        return new ResponseEntity<>(citesphereConnector.getCitesphereApps(), HttpStatus.OK);
    }
}
