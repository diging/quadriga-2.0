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
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.api.v1.model.GraphPatternList;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
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

    @ResponseBody
    @PostMapping(value = "/api/v1/collection/{collectionId}/network/map")
    public HttpStatus mapPatternToTriples(@PathVariable String collectionId,
            @RequestBody GraphPatternList graphPatternList) {

        List<EventGraph> eventGraphs = eventGraphService.getEventGraphs(new ObjectId(collectionId));
        if (eventGraphs == null) {
            return HttpStatus.NOT_FOUND;
        }

        for (EventGraph eventGraph : eventGraphs) {
            for (GraphPattern graphPattern : graphPatternList.getPatternMappings()) {
                MappedTripleGroup mappedTripleGroup;
                try {
                    if (graphPattern.getMappedTripleGroupId() != null && !graphPattern.getMappedTripleGroupId().isEmpty()) {
                        mappedTripleGroup = mappedTripleGroupService.getById(graphPattern.getMappedTripleGroupId());
                    } else {
                        mappedTripleGroup = mappedTripleGroupService.get(collectionId, MappedTripleType.DEFAULT_MAPPING);
                    }
                } catch (InvalidObjectIdException | CollectionNotFoundException e) {
                    logger.error("No collection found with id {}", collectionId, e);
                    return HttpStatus.NOT_FOUND;
                }
                
                List<Graph> extractedGraphs = patternFinder.findGraphsWithPattern(graphPattern, eventGraph);
                for (Graph extractedGraph : extractedGraphs) {
                    try {
                        mappedTripleService.storeMappedGraph(extractedGraph, mappedTripleGroup);
                    } catch (NodeNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return HttpStatus.OK;

    }

}
