package edu.asu.diging.quadriga.api.v1.model;

import edu.asu.diging.quadriga.core.model.DefaultMapping;

public class Metadata {

    private DefaultMapping defaultMapping;
    private Context context;
    
    public DefaultMapping getDefaultMapping() {
        return defaultMapping;
    }
    public void setDefaultMapping(DefaultMapping defaultMapping) {
        this.defaultMapping = defaultMapping;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    
}
