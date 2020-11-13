package edu.asu.diging.quadriga.core.model.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

import edu.asu.diging.quadriga.core.model.IApp;

@Entity
public class App implements IApp {

    @Id
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
