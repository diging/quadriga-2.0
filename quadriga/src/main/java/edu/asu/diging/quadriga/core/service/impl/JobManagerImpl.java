package edu.asu.diging.quadriga.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.data.JobRepository;
import edu.asu.diging.quadriga.core.model.jobs.Job;
import edu.asu.diging.quadriga.core.model.jobs.JobStatus;
import edu.asu.diging.quadriga.core.service.JobManager;

@Service
public class JobManagerImpl implements JobManager {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public String createJob(String collectionId, String mappedTripleGroupId, int totalNetworks) {
        Job job = new Job();
        job.setCollectionId(collectionId);
        job.setMappedTripleGroupId(mappedTripleGroupId);
        job.setProcessedNetworks(0);
        job.setTotalNetworks(totalNetworks);
        job.setStatus(JobStatus.STARTED);
        job = jobRepository.save(job);
        return job.getId();
    }

    @Override
    public Job get(String jobId) {
        return jobRepository.findById(jobId).orElse(null);
    }

}
