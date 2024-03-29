package edu.asu.diging.quadriga.core.model.elements;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {

    @Id
    private String id;

    @Transient
    private String refId;

    private Actor creator;

    private String creation_date;

    private Place creation_place;

    public String getId() {
        return id;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefId() {
        return refId;
    }

    @XmlElement(type = Actor.class)
    public Actor getCreator() {
        return creator;
    }

    public String getCreationDate() {
        return creation_date;
    }

    public Place getCreationPlace() {
        return creation_place;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreator(Actor actor) {
        this.creator = actor;
    }

    public void setCreationDate(String date) {
        this.creation_date = date;
    }

    public void setCreationPlace(Place place) {
        this.creation_place = place;
    }

}
