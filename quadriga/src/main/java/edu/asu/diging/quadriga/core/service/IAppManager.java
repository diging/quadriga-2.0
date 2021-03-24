package edu.asu.diging.quadriga.core.service;

import edu.asu.diging.quadriga.core.model.IApp;
import edu.asu.diging.quadriga.web.forms.AppForm;

public interface IAppManager {

    IApp addApp(AppForm appForm);
}
