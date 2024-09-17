package edu.asu.diging.quadriga.api.v1;

import java.util.List;



import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.asu.diging.quadriga.api.v1.model.JobPatternInfo;
import edu.asu.diging.quadriga.api.v1.model.PatternMapping;
import edu.asu.diging.quadriga.api.v1.service.GraphTripleMapper;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Controller
public class GraphTripleMapperController {

    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private GraphTripleMapper graphTripleMapper;
    
    @Autowired
    private CollectionManager collectionManager;
    
    @PostMapping(value = "/api/v1/collection/{collectionId}/map")
    public ResponseEntity<List<JobPatternInfo>> mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody List<PatternMapping> patternMappingList) throws InvalidObjectIdException, CollectionNotFoundException {
               
        int pageNumber = 0; // Set initial page number
        int pageSize = 10;
        if(collectionManager.findCollection(collectionId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<EventGraph> eventGraphs = eventGraphService.getEventGraphsByCollectionId(new ObjectId(collectionId), PageRequest.of(pageNumber, pageSize));
        List<JobPatternInfo> jobInfos = graphTripleMapper.mapPatterns(collectionId, eventGraphs, patternMappingList);
        
        if(!jobInfos.isEmpty()) {
            return new ResponseEntity<>(jobInfos, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}