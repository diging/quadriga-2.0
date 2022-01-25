package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.SimpleUserAppRepository;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;

@Service
public class SimpleUserAppServiceImpl implements SimpleUserAppService {
    
    @Autowired
    private SimpleUserAppRepository simpleUserAppRepository;
    
    @Override
    public SimpleUserApp save(SimpleUserApp simpleUserApp) {
        return simpleUserAppRepository.save(simpleUserApp);
    }

    @Override
    public List<SimpleUserApp> findByUsername(String username) {
        return simpleUserAppRepository.findByUsername(username);
    }
    
    @Override
    public SimpleUserApp findByUsernameAndAppClientId(String username, String appClientId) {
        return simpleUserAppRepository.findByUsernameAndAppClientId(username, appClientId);
    }
    
    @Override
    public void deleteByUsernameAndAppClientId(String username, String appClientId) {
        simpleUserAppRepository.deleteByUsernameAndAppClientId(username, appClientId);
    }

}
