package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.SimpleUserAppRepository;
import edu.asu.diging.quadriga.core.exceptions.SimpleUserAppNotFoundException;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

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
    private SimpleUserAppRepository simpleUserAppRepository;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.SimpleUserAppService#save(java.lang.String, java.lang.String)
     */
    @Override
    public SimpleUserApp save(String username, String clientId) {
        SimpleUserApp simpleUserApp = new SimpleUserApp();
        simpleUserApp.setUsername(username);
        simpleUserApp.setAppClientId(clientId);
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
    public void delete(String username, String appClientId) throws SimpleUserAppNotFoundException {
        SimpleUserApp userApp = simpleUserAppRepository.findByUsernameAndAppClientId(username, appClientId);
        if (userApp != null) {
            simpleUserAppRepository.delete(userApp);
        }else {
            throw new SimpleUserAppNotFoundException(); 
        }
    }
    
    @Override
    public List<SimpleUserApp> findByUsername(String username,int offset,int pageSize){        
        return simpleUserAppRepository.findByUsernameWithPagination(username,offset,pageSize);  
    }

    @Override
    public List<String> findAppClientIdsByUsername(String username){
        return simpleUserAppRepository.findAppClientIdsByUsername(username);          
        
    }
}
