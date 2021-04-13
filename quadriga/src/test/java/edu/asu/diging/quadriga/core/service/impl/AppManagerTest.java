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
    private AppForm appForm;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        app1 = new App();
        app1.setId(ID1);
        app1.setName("name");
        app1.setDescription("testApp");
        appForm = new AppForm();
        appForm.setName("name");
        appForm.setDescription("testApp");
        Mockito.when(appRepo.saveApp(appForm)).thenReturn(app1);

    }

    @Test
    public void test_addApp_success() {
        String name = "name";
        String desc = "testApp";
        App app = managerToTest.addApp(appForm);
        Mockito.verify(appRepo).saveApp(appForm);
        Assert.assertEquals(name, app.getName());
        Assert.assertEquals(desc, app.getDescription());
        Assert.assertEquals(ID1, app.getId());
    }

}
