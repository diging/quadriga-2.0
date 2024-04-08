package edu.asu.diging.quadriga.api.v1;

import java.util.List;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.service.MapGraphToTriple;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.JobManager;

@Controller
public class MapGraphToTripleController {

    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private MapGraphToTriple mapGraphToTriple;

    @Autowired
    private JobManager jobManager;

    @PostMapping(value = "/api/v1/collection/{collectionId}/map")
    public ResponseEntity<List<JobPatternInfo>> mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody List<PatternMapping> patternMappingList) {

        List<EventGraph> eventGraphs = eventGraphService.getEventGraphs(new ObjectId(collectionId));
        
        if (eventGraphs == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
               
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(new ObjectId());
        mappedTripleGroup.setCollectionId(new ObjectId(collectionId));
        
        String jobId = jobManager.createJob(collectionId, mappedTripleGroup.get_id().toString(), eventGraphs.size());
        
        List<JobPatternInfo> jobInfos = mapGraphToTriple.mapPatterns(collectionId, jobId, eventGraphs, patternMappingList);
        
        if(!jobInfos.isEmpty()) {
            return new ResponseEntity<>(jobInfos, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}