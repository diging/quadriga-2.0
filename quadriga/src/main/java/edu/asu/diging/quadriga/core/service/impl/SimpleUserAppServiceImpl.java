package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.SimpleUserAppRepository;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

/**
 * This class provides a Service for the repository SimpleUserAppRepository to
 * apply operations on the SimpleUserApp table in the database
 * 
 * @author poojakulkarni
 *
 */
@Service
public class SimpleUserAppServiceImpl implements SimpleUserAppService {

    @Autowired
    private CitesphereConnector citesphereConnector;

    @Autowired
    private SimpleUserAppRepository simpleUserAppRepository;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.SimpleUserAppService#save(edu.asu.diging.quadriga.core.model.users.SimpleUserApp)
     */
    @Override
    public SimpleUserApp save(SimpleUserApp simpleUserApp) {
        return simpleUserAppRepository.save(simpleUserApp);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.SimpleUserAppService#findByUsername(java.lang.String)
     */
    @Override
    public List<SimpleUserApp> findByUsername(String username) {
        return simpleUserAppRepository.findByUsername(username);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.SimpleUserAppService#delete(java.lang.String, java.lang.String)
     */
    @Override
    public void delete(String username, String appClientId) {
        SimpleUserApp userApp = simpleUserAppRepository.findByUsernameAndAppClientId(username, appClientId);
        if (userApp != null) {
            simpleUserAppRepository.delete(userApp);
        }
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.SimpleUserAppService#getCitesphereApps(edu.asu.diging.simpleusers.core.model.impl.SimpleUser)
     */
    @Override
    public List<CitesphereAppInfo> getCitesphereApps(SimpleUser user) {
        // Get the set of apps that are accessible by the user
        Set<String> userAppClientIds = findByUsername(user.getUsername()).stream()
                .map(userApp -> userApp.getAppClientId()).collect(Collectors.toSet());

        // Get all apps from citesphere and filter them to only display apps accessible
        // by the user
        List<CitesphereAppInfo> apps = new ArrayList<>();

        if (!userAppClientIds.isEmpty()) {
            apps = citesphereConnector.getCitesphereApps().stream()
                    .filter(app -> userAppClientIds.contains(app.getClientId())).collect(Collectors.toList());
        }
        return apps;
    }

}
