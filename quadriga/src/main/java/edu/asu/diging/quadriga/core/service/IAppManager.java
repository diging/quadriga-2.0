package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.exceptions.UnstorableObjectException;
import edu.asu.diging.quadriga.web.forms.AppForm;

public interface IAppManager {

    void addApp(AppForm appForm) throws UnstorableObjectException;
}