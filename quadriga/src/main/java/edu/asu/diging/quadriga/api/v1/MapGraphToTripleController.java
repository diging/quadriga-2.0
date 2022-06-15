package edu.asu.diging.quadriga.api.v1;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.asu.diging.quadriga.api.v1.model.GraphPatternList;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Controller
public class MapGraphToTripleController {
    
    @Autowired
    private EventGraphService eventGraphService;
    
    @PostMapping(value="/api/v1/collection/{collectionId}/network/map")
    public HttpStatus mapPatternToTriples(@PathVariable String collectionId, @RequestBody GraphPatternList graphPatternList) {
        
        EventGraph eventGraph = eventGraphService.findLatestEventGraphByCollectionId(new ObjectId(collectionId));
        if (eventGraph == null) {
            return HttpStatus.NOT_FOUND;
        }
        
        
    }

}
