package edu.asu.diging.quadriga.api.v1;

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
import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Controller
public class AddNetworkApiController {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private CollectionManager collectionManager;

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
    @InjectToken
    @ResponseBody
    @RequestMapping(value = "/api/v1/collection/{collectionId}/network/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @PathVariable String collectionId, TokenInfo tokenInfo) {

        try {
            Collection collection = collectionManager.findCollection(collectionId);
            if (collection.getApps() == null || collection.getApps().isEmpty()
                    || !collection.getApps().contains(tokenInfo.getClient_id())) {
                return HttpStatus.UNAUTHORIZED;
            }
        } catch (InvalidObjectIdException e) {
            return HttpStatus.NOT_FOUND;
        }
        
        // First we check whether a quadruple is present in request body
        if (quadruple == null) {
            logger.error("Quadruple not present in network submission request for collectionId: "  + collectionId);
            return HttpStatus.BAD_REQUEST;
        }
        
        // Next, we check whether a collection and mappedTripleGroup is present
        // Every time a new network is submitted, the triple in that network has to be added as the
        // default MappedTripleGroup for given collectionId
        MappedTripleGroup mappedTripleGroup;
        try {
            mappedTripleGroup = mappedTripleGroupService.get(collectionId, MappedTripleType.DEFAULT_MAPPING);
            if(mappedTripleGroup == null) {
                return HttpStatus.NOT_FOUND;
            }
        } catch(InvalidObjectIdException | CollectionNotFoundException e)  {
            logger.error("Couldn't submit network", e);
            return HttpStatus.NOT_FOUND;
        }

        eventGraphService.mapNetworkAndSave(quadruple.getGraph(), collectionId);
 
        try {
            // The new MappedTripleGroup's Id has to be added to Concepts and Predicates
            mappedTripleService.storeMappedGraph(quadruple.getGraph(), mappedTripleGroup);
        } catch (NodeNotFoundException e1) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.ACCEPTED;

    }

  

}