package edu.asu.diging.quadriga.core.service.impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.SimpleUserAppRepository;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;

public class SimpleUserAppServiceImplTest {
    
    @InjectMocks
    private SimpleUserAppServiceImpl simpleUserAppService;
    
    @Mock
    private CitesphereConnector citesphereConnector;

    @Mock
    private SimpleUserAppRepository simpleUserAppRepository;
    
    private SimpleUserApp USER_APP_1;
    private SimpleUserApp USER_APP_2;
    private List<SimpleUserApp> USER_APP_LIST;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        String user = "user";
        String clientId1 = "clientId1";
        String clientId2 = "clientId2";
        
        USER_APP_1 = new SimpleUserApp();
        USER_APP_1.setId("1");
        USER_APP_1.setUsername(user);
        USER_APP_1.setAppClientId(clientId1);
        USER_APP_LIST.add(USER_APP_1);
        
        USER_APP_2 = new SimpleUserApp();
        USER_APP_2.setId("2");
        USER_APP_2.setUsername(user);
        USER_APP_2.setAppClientId(clientId2);
        USER_APP_LIST.add(USER_APP_2);
    }
    
    @Test
    public void test_findByUsername_success() {
        
    }
    
    @Test
    public void test_findByUsername_empty() {
        
    }
    
    @Test
    public void test_findByUsernameAndAppClientId_success() {
        
    }
    
    @Test
    public void test_findByUsernameAndAppClientId_empty() {
        
    }
    
    @Test
    public void test_delete_success() {
        
    }
    
    @Test
    public void test_delete_empty() {
        
    }
    
    @Test
    public void test_getCitesphereApps_allMatching() {
        
    }
    
    @Test
    public void test_getCitesphereApps_partialMatching() {
        
    }
    
    @Test
    public void test_getCitesphereApps_noMatching() {
        
    }

}
