package edu.asu.diging.quadriga.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.Parameter;

import org.hibernate.annotations.GenericGenerator;

import edu.asu.diging.quadriga.core.model.IApp;

@Entity
public class App implements IApp {

    @Id
    @GeneratedValue(generator = "app_id_generator")
    @GenericGenerator(name = "app_id_generator", parameters = @Parameter(name = "prefix", value = "APP"), strategy = "edu.asu.diging.quadriga.core.data.IdGenerator")
    private String id;
    private String name;
    private String description;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
