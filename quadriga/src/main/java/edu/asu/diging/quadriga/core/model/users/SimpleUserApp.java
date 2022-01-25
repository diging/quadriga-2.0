package edu.asu.diging.quadriga.core.model.users;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * This class represents the table 'SimpleUserApp' in the database
 * that stores information about the citesphere apps assigned to users
 * 
 * @author poojakulkarni
 *
 */
@Entity
@Table
public class SimpleUserApp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8474699901885186804L;
    
    @Id
    private String id;
    
    private String username;
    private String appClientId;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getAppClientId() {
        return appClientId;
    }
    
    public void setAppClientId(String appClientId) {
        this.appClientId = appClientId;
    }
    
    
}
