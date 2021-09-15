package edu.asu.diging.quadriga.web.forms;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public class CollectionForm {
    @NotNull
    @Size(min=1)
    private String name;

    private String description;
    
    private List<CitesphereAppInfo> apps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CitesphereAppInfo> getApps() {
        return apps;
    }

    public void setApps(List<CitesphereAppInfo> apps) {
        this.apps = apps;
    }
    
}
