package edu.asu.diging.quadriga.core.db;

import edu.asu.diging.quadriga.core.exceptions.UnstorableObjectException;
import edu.asu.diging.quadriga.core.model.IApp;

public interface IAppDbConnection {
    
    IApp getById(String id);

    IApp store(IApp app) throws UnstorableObjectException;
    
    String generateId();
}
