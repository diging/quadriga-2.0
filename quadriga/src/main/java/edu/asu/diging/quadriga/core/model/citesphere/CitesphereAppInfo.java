package edu.asu.diging.quadriga.core.model.citesphere;

import java.util.Set;

/**
 * Holds the citesphere application information
 * @author Maulik Limbadiya
 *
 */
public class CitesphereAppInfo {
    private String clientId;
    private String name;
    private String description;
    private Set<String> applicationTypes;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

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

    public Set<String> getApplicationTypes() {
        return applicationTypes;
    }

    public void setApplicationTypes(Set<String> applicationTypes) {
        this.applicationTypes = applicationTypes;
    }
}
