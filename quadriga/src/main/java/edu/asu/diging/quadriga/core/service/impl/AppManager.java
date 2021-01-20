package edu.asu.diging.quadriga.core.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.AppRepository;
import edu.asu.diging.quadriga.core.model.IApp;
import edu.asu.diging.quadriga.core.model.impl.App;
import edu.asu.diging.quadriga.core.service.IAppManager;
import edu.asu.diging.quadriga.web.forms.AppForm;

@Service
@Transactional
public class AppManager implements IAppManager {

    @Autowired
    private AppRepository appRepo;

    @Override
    public void addApp(AppForm appForm) {
        IApp app = new App();
        app.setName(appForm.getName());
        app.setDescription(appForm.getDescription());
        appRepo.save((App)app);
    }

}