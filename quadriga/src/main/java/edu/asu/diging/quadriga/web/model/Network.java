package edu.asu.diging.quadriga.web.model;

import java.time.LocalDateTime;

/**
 * A model to display networks on the collection page
 * @author poojakulkarni
 *
 */
public class Network {
    
    private String sourceURI;
    private LocalDateTime creationTime;
    private String creator;
    private String appName;
    
    public String getSourceURI() {
        return sourceURI;
    }
    public void setSourceURI(String sourceURI) {
        this.sourceURI = sourceURI;
    }
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }

}
