package edu.asu.diging.quadriga.core.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.AppRepository;
import edu.asu.diging.quadriga.core.model.impl.App;
import edu.asu.diging.quadriga.core.service.IAppManager;
import edu.asu.diging.quadriga.web.forms.AppForm;

@Service
@Transactional
public class AppManager implements IAppManager {

    @Autowired
    private AppRepository appRepo;

    
    
    /**
     * Creates a new App instance and stores it in the database
     * 
     * @param appForm   Form data that needs to be added to database
     * 
     * 
     * @return App Instance that is saved in database
     * 
     **/
    @Override
    public App addApp(AppForm appForm, App app) {
        app.setName(appForm.getName());
        app.setDescription(appForm.getDescription());
        app = appRepo.save(app);
        return app;
    }

}