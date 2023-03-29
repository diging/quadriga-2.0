package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.data.SimpleUserAppRepository;
import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;
import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public class SimpleUserAppServiceImplTest {

    @InjectMocks
    private SimpleUserAppServiceImpl simpleUserAppService;

    @Mock
    private CitesphereConnector citesphereConnector;

    @Mock
    private SimpleUserAppRepository simpleUserAppRepository;

    private String USER_1;
    private String USER_2;
    private SimpleUser SIMPLE_USER_1;
    private String CLIENT_ID_1;
    private String CLIENT_ID_2;
    private String CLIENT_ID_3;
    private SimpleUserApp USER_APP_1;
    private SimpleUserApp USER_APP_2;
    private List<SimpleUserApp> USER_APP_LIST;
    private CitesphereAppInfo APP_1;
    private CitesphereAppInfo APP_2;
    private CitesphereAppInfo APP_3;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        USER_1 = "user1";
        USER_2 = "user2";

        SIMPLE_USER_1 = new SimpleUser();
        SIMPLE_USER_1.setUsername(USER_1);

        CLIENT_ID_1 = "clientId1";
        CLIENT_ID_2 = "clientId2";
        CLIENT_ID_3 = "clientId3";
        
        USER_APP_LIST = new ArrayList<>();

        USER_APP_1 = new SimpleUserApp();
        USER_APP_1.setId("1");
        USER_APP_1.setUsername(USER_1);
        USER_APP_1.setAppClientId(CLIENT_ID_1);
        USER_APP_LIST.add(USER_APP_1);

        USER_APP_2 = new SimpleUserApp();
        USER_APP_2.setId("2");
        USER_APP_2.setUsername(USER_1);
        USER_APP_2.setAppClientId(CLIENT_ID_2);
        USER_APP_LIST.add(USER_APP_2);

        APP_1 = new CitesphereAppInfo();
        APP_1.setClientId(CLIENT_ID_1);

        APP_2 = new CitesphereAppInfo();
        APP_2.setClientId(CLIENT_ID_2);

        APP_3 = new CitesphereAppInfo();
        APP_3.setClientId(CLIENT_ID_3);
    }

    @Test
    public void test_findByUsername_success() {
        Mockito.when(simpleUserAppRepository.findByUsername(USER_1)).thenReturn(USER_APP_LIST);
        List<SimpleUserApp> response = simpleUserAppService.findByUsername(USER_1);
        Assert.assertNotEquals(0, response.size());
        for (SimpleUserApp app : response) {
            Assert.assertTrue(USER_APP_LIST.stream().anyMatch(userapp -> userapp.getId().equals(app.getId())));
        }
    }

    @Test
    public void test_findByUsername_empty() {
        Mockito.when(simpleUserAppRepository.findByUsername(USER_2)).thenReturn(new ArrayList<>());
        List<SimpleUserApp> actualResponse = simpleUserAppService.findByUsername(USER_2);
        Assert.assertNotNull(actualResponse);
        Assert.assertEquals(0, actualResponse.size());
    }
    
    @Test
    public void test_delete_success() {
        Mockito.when(simpleUserAppRepository.findByUsernameAndAppClientId(USER_1, CLIENT_ID_1)).thenReturn(USER_APP_1);
        simpleUserAppService.delete(USER_1, CLIENT_ID_1);
        Mockito.verify(simpleUserAppRepository).delete(USER_APP_1);
    }
    
    @Test
    public void test_delete_noEntry() {
        Mockito.when(simpleUserAppRepository.findByUsernameAndAppClientId(USER_1, CLIENT_ID_1)).thenReturn(null);
        simpleUserAppService.delete(USER_1, CLIENT_ID_1);
        Mockito.verify(simpleUserAppRepository, Mockito.times(0)).delete(Mockito.any(SimpleUserApp.class));
    }
        
    

    @Test
    public void test_getCitesphereApps_allMatching() {
        List<CitesphereAppInfo> citesphereApps = new ArrayList<>();
        citesphereApps.add(APP_1);
        citesphereApps.add(APP_2);

        Mockito.when(simpleUserAppRepository.findByUsername(USER_1)).thenReturn(USER_APP_LIST);
        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);

        List<CitesphereAppInfo> response = simpleUserAppService.getCitesphereApps(SIMPLE_USER_1);

        for (CitesphereAppInfo app : citesphereApps) {
            Assert.assertTrue(response.stream().anyMatch(responseApp -> responseApp.getClientId().equals(app.getClientId())));
        }
    }

    @Test
    public void test_getCitesphereApps_partialMatching() {
        List<CitesphereAppInfo> citesphereApps = new ArrayList<>();
        citesphereApps.add(APP_2);
        citesphereApps.add(APP_3);

        Mockito.when(simpleUserAppRepository.findByUsername(USER_1)).thenReturn(USER_APP_LIST);
        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);

        List<CitesphereAppInfo> response = simpleUserAppService.getCitesphereApps(SIMPLE_USER_1);
        
        Assert.assertEquals(1, response.size());
        Assert.assertTrue(response.get(0).getClientId().equals(CLIENT_ID_2));
    }

    @Test
    public void test_getCitesphereApps_noMatching() {
        List<CitesphereAppInfo> citesphereApps = new ArrayList<>();
        citesphereApps.add(APP_3);

        Mockito.when(simpleUserAppRepository.findByUsername(USER_1)).thenReturn(USER_APP_LIST);
        Mockito.when(citesphereConnector.getCitesphereApps()).thenReturn(citesphereApps);

        List<CitesphereAppInfo> response = simpleUserAppService.getCitesphereApps(SIMPLE_USER_1);
        
        Assert.assertEquals(0, response.size());
    }

}
