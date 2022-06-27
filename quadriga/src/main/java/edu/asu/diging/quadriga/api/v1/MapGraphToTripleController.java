package edu.asu.diging.quadriga.api.v1;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.api.v1.model.GraphPatternList;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.PatternFinder;

@Controller
public class MapGraphToTripleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private PatternFinder patternFinder;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @PostMapping(value = "/api/v1/collection/{collectionId}/network/map")
    public HttpStatus mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody GraphPatternList graphPatternList) {

        EventGraph eventGraph = eventGraphService.findLatestEventGraphByCollectionId(new ObjectId(collectionId));
        if (eventGraph == null) {
            return HttpStatus.NOT_FOUND;
        }

        for (GraphPattern graphPattern : graphPatternList.getPatternMappings()) {
            MappedTripleGroup mappedTripleGroup;
            try {
                mappedTripleGroup = mappedTripleGroupService.getById(graphPattern.getMappedTripleGroupId());
                if (mappedTripleGroup == null) {
                    return HttpStatus.NOT_FOUND;
                }
            } catch (InvalidObjectIdException e) {
                logger.error("Couldn't submit network", e);
                return HttpStatus.NOT_FOUND;
            }
            List<Graph> extractedGraphs = patternFinder.findGraphsWithPattern(graphPattern, eventGraph);
            for (Graph extractedGraph : extractedGraphs) {
                try {
                    mappedTripleService.storeMappedGraph(extractedGraph, mappedTripleGroup);
                } catch (NodeNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return HttpStatus.OK;

    }

}
