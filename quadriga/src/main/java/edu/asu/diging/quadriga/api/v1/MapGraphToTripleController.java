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

import edu.asu.diging.quadriga.api.v1.model.GraphPatternList;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.AsyncPatternProcessor;
import edu.asu.diging.quadriga.core.service.EventGraphService;

@Controller
public class MapGraphToTripleController {

    @Autowired
    private AsyncPatternProcessor asyncPatternProcessor;

    @Autowired
    private EventGraphService eventGraphService;

    @PostMapping(value = "/api/v1/collection/{collectionId}/network/map")
    public ResponseEntity<List<String>> mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody GraphPatternList graphPatternList) {

        List<EventGraph> eventGraphs = eventGraphService.getEventGraphs(new ObjectId(collectionId));
        if (eventGraphs == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                asyncPatternProcessor.processPatterns(collectionId, graphPatternList.getPatternMappings(), eventGraphs),
                HttpStatus.OK);

    }

}
