package edu.asu.diging.quadriga.web.apps;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.diging.quadriga.core.citesphere.ICitesphereConnector;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

@Controller
public class CitesphereAppsInfoController {

    @Autowired
    private ICitesphereConnector citesphereConnector;

    @GetMapping("/admin/citesphere/apps")
    public ResponseEntity<List<CitesphereAppInfo>> getCitesphereApps() {
        return new ResponseEntity<List<CitesphereAppInfo>>(citesphereConnector.getCitesphereApps(), HttpStatus.OK);
    }
}
