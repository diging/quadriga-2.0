package edu.asu.diging.quadriga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.diging.quadriga.api.v1.model.Quadruple;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.NetworkMapper;

@Controller
public class AddNetworkApiController {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleService mappedTripleService;
    
    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    /**
     * The method parse given Json from the post request body and add Network
     * instance to the database
     * 
     * @param request
     * @param response
     * @param xml
     * @param accept
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/api/v1/collection/{collectionId}/network/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @PathVariable String collectionId) {
        
        // Every time a new network is submitted, the triple in that network has to be added as the
        // default MappedTripleGroup for given collectionId
        MappedTripleGroup mappedTripleGroup;
        try {
            mappedTripleGroup = mappedTripleGroupService.get(collectionId, MappedTripleType.DEFAULT_MAPPING);
            if(mappedTripleGroup == null) {
                logger.error("Couldn't find or persist a new MappedTripleGroup entry for collectionId: " + collectionId);
                return HttpStatus.NOT_FOUND;
            }
        } catch(InvalidObjectIdException | CollectionNotFoundException e)  {
            logger.error("Couldn't submit network", e);
            return HttpStatus.NOT_FOUND;
        }

        if (quadruple == null) {
            return HttpStatus.NO_CONTENT;
        }
        
        // the flow will reach  here  only when the citesphere token is present, valid and active

        // save network
        List<CreationEvent> events = networkMapper.mapNetworkToEvents(quadruple.getGraph());
        List<EventGraph> eventGraphs = events.stream().map(e -> new EventGraph(e)).collect(Collectors.toList());
        eventGraphs.forEach(e -> {
            e.setCollectionId(new ObjectId(collectionId));
            e.setDefaultMapping(quadruple.getGraph().getMetadata().getDefaultMapping());
        });
        eventGraphService.saveEventGraphs(eventGraphs);

        try {
            // The new MappedTripleGroup's Id has to be added to Concepts and Predicates
            mappedTripleService.storeMappedGraph(quadruple.getGraph(), mappedTripleGroup);
        } catch (NodeNotFoundException e1) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.ACCEPTED;

    }

}