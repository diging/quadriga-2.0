package edu.asu.diging.quadriga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.diging.quadriga.api.v1.model.Quadruple;
import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.impl.CitesphereConnectorImpl;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
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

    @Autowired
    private CitesphereConnectorImpl citesphereConnectorImpl;

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
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @PathVariable String collectionId, @RequestHeader(name = "Authorization",  required = true) String authHeader) {
        // First we check whether a quadruple is present in request body
        if (quadruple == null) {
            logger.error("Quadruple not present in network submission request for collectionId: "  + collectionId);
            return HttpStatus.NO_CONTENT;
        }

        // Next, we check validity of token received from Vogon
        String token = getTokenFromHeader(authHeader);
        if(token == null) {
            logger.error("Token not present in AuthorizationHeader for collectionId: " + collectionId);
            return HttpStatus.NOT_FOUND;
        }

        TokenInfo tokenInfo;
        try {
            tokenInfo = citesphereConnectorImpl.getTokenInfo(token);
            
            // either token info wasn't returned by citesphere or the token has expired
            if(tokenInfo == null || !tokenInfo.isActive()) {
                return HttpStatus.UNAUTHORIZED;
            }
        } catch (OAuthException e) {
            // we got unauth twice (using existing access token and re-generated one)
            return HttpStatus.UNAUTHORIZED;
        } catch(BadCredentialsException e) {
            //Token is invalid
            return HttpStatus.FORBIDDEN;
        }
        if(tokenInfo == null || !tokenInfo.isActive()) {
            return HttpStatus.UNAUTHORIZED;
        }
        // The flow will reach  here  only when token is present, valid  and active
        // Next, we check whether a collection and mappedTripleGroup is present
        // Every time a new network is submitted, the triple in that network has to be added as the
        // default MappedTripleGroup for given collectionId
        MappedTripleGroup mappedTripleGroup;
        try {
            mappedTripleGroup = mappedTripleGroupService.getMappedTripleGroup(collectionId, MappedTripleType.DEFAULT_MAPPING);
            if(mappedTripleGroup == null) {
                return HttpStatus.NOT_FOUND;
            }
        } catch(InvalidObjectIdException | CollectionNotFoundException e)  {
            logger.error(e.getMessage());
            return HttpStatus.NOT_FOUND;
        }

        // save network
        List<CreationEvent> events = networkMapper.mapNetworkToEvents(quadruple.getGraph());
        List<EventGraph> eventGraphs = events.stream().map(e -> new EventGraph(e)).collect(Collectors.toList());
        eventGraphs.forEach(e -> {
            e.setCollectionId(new ObjectId(collectionId));
            e.setDefaultMapping(quadruple.getGraph().getMetadata().getDefaultMapping());
            e.setContext(quadruple.getGraph().getMetadata().getContext());

            /**
             * This is a temporary solution as per the comment on story Q20-18
             * A new story will later be created to get info about just one app from citesphere using OAuth token.
             * This app's name should be stored in eventGraph instead of the client id
             */
            e.setAppName(tokenInfo.getClient_id());
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
    
    
    /**
     * This method will check and get a token that should be present in the
     * Authorization Header of the add network request
     * 
     * The token will be in the form of "Bearer xxxxxxx"
     * 
     * @param authHeader is the header to be checked
     * @return
     */
    private String getTokenFromHeader(String authHeader) {
        String token;
        
        if(authHeader == null || authHeader.trim().isEmpty()) {
            return null;
        } else {
            // Trims the string "Bearer " to extract the exact token from the Authorization Header
            token = authHeader.substring(7);
            if(token == null || token.trim().isEmpty()) {
                return null;
            }
        }
        return token;
    }

}