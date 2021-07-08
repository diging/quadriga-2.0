package edu.asu.diging.quadriga.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CollectionForm {
    @NotNull
    @Size(min=1)
    private String name;

    private String description;

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

}
