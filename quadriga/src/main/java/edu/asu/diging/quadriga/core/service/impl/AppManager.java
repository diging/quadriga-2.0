package edu.asu.diging.quadriga.core.service.impl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.db.IAppDbConnection;
import edu.asu.diging.quadriga.core.exceptions.UnstorableObjectException;
import edu.asu.diging.quadriga.core.model.IApp;
import edu.asu.diging.quadriga.core.model.impl.App;
import edu.asu.diging.quadriga.core.service.IAppManager;
import edu.asu.diging.quadriga.web.forms.AppForm;

@Service
@Transactional
public class AppManager implements IAppManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IAppDbConnection dbConnection;

    @Override
    public void addApp(AppForm appForm) throws UnstorableObjectException {
        IApp app = new App();
        app.setId(dbConnection.generateId());
        app.setName(appForm.getName());
        app.setDescription(appForm.getDescription());
        try {
            dbConnection.store(app);
        } catch (UnstorableObjectException e) {
            // should never happen, we're setting the id
            logger.error("Could not store app.", e);
        }
    }
}
