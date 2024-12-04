package edu.asu.diging.quadriga.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.asu.diging.quadriga.core.model.jobs.Job;

public interface JobRepository extends PagingAndSortingRepository<Job, String> {

}
