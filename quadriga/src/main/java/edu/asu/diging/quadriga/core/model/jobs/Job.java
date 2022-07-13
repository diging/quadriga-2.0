package edu.asu.diging.quadriga.core.model.jobs;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class Job {

    @Id
    @GeneratedValue(generator = "job_id_generator")
    @GenericGenerator(name = "job_id_generator", parameters = @Parameter(name = "prefix", value = "JOB"), strategy = "edu.asu.diging.quadriga.core.data.IdGenerator")
    private String id;

    private String collectionId;

    private String mappedTripleGroupId;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private int processedNetworks;

    private int totalNetworks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public int getProcessedNetworks() {
        return processedNetworks;
    }

    public void setProcessedNetworks(int processedNetworks) {
        this.processedNetworks = processedNetworks;
    }

    public int getTotalNetworks() {
        return totalNetworks;
    }

    public void setTotalNetworks(int totalNetworks) {
        this.totalNetworks = totalNetworks;
    }

    public String getMappedTripleGroupId() {
        return mappedTripleGroupId;
    }

    public void setMappedTripleGroupId(String mappedTripleGroupId) {
        this.mappedTripleGroupId = mappedTripleGroupId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

}
