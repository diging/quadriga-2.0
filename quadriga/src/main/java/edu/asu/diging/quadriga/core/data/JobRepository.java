package edu.asu.diging.quadriga.core.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.asu.diging.quadriga.core.model.jobs.Job;

public interface JobRepository extends MongoRepository<Job, String> {

}
