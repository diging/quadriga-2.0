package edu.asu.diging.quadriga.core.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.asu.diging.quadriga.core.data.AppRepository;
import edu.asu.diging.quadriga.core.model.impl.App;
import edu.asu.diging.quadriga.web.forms.AppForm;


public class AppManagerTest {

    
    @Mock
    private AppRepository appRepo;
    
    @InjectMocks
    private AppManager managerToTest;
    
    private App app1;
    private String ID1 = "ID1";
   
    
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        app1 = new App();
        app1.setId(ID1);
        app1.setName("name");
        app1.setDescription("testApp");
        Mockito.when(appRepo.save(app1)).thenReturn(app1);
        
    }
    
    @Test
    public void test_addApp_success() {
        AppForm appForm = new AppForm();
        String name = "name";
        String desc = "testApp";
        appForm.setName(name);
        appForm.setDescription(desc);
        App app = managerToTest.addApp(appForm);
        Mockito.verify(appRepo).save(app);
        Assert.assertEquals(name,app.getName());
        Assert.assertEquals(desc,app.getDescription());
    }
    
}
