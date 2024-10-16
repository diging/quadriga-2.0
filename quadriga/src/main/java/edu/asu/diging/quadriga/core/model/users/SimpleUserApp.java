package edu.asu.diging.quadriga.core.model.users;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * This class represents the table 'SimpleUserApp' in the database that stores
 * information about the citesphere apps assigned to users and maps them based on appClientId
 * 
 * @author poojakulkarni
 *
 */
@Entity
@Table(name = "SimpleUserApp")
public class SimpleUserApp implements Serializable {

    private static final long serialVersionUID = -8474699901885186804L;
    
    /**
    * A generator used to generate unique IDs for instances of the UserApp class.
    * The prefix parameter specifies the prefix to be used in the generated value
    * Strategy parameter for the generator is specified for generating a unique identifier.
    */
    @Id
    @GeneratedValue(generator = "userapp_id_generator")
    @GenericGenerator(name = "userapp_id_generator", parameters = @Parameter(name = "prefix", value = "UA"), strategy = "edu.asu.diging.quadriga.core.data.sql.IdGenerator")
    private String id;
    /**
     * User name of the user 
     */
    private String username;
    /**
     * Citesphere client Id
     */
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
