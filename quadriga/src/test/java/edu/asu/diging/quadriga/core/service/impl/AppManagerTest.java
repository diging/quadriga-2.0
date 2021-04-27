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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_addApp_success() {
        AppForm appForm = new AppForm();
        String name = "name";
        String description = "testApp";
        appForm.setName(name);
        appForm.setDescription(description);
        App app = managerToTest.addApp(appForm);
        Mockito.verify(appRepo).save(app);
        Assert.assertEquals(name, app.getName());
        Assert.assertEquals(description, app.getDescription());
    }

}
