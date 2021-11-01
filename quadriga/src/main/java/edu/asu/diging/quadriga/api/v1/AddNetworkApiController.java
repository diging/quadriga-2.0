package edu.asu.diging.quadriga.api.v1;

import java.util.List;
import java.util.stream.Collectors;

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
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.NetworkMapper;

@Controller
public class AddNetworkApiController {

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private CitesphereConnectorImpl citesphereConnectorImpl;

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedCollectionService mappedCollectionService;

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
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @PathVariable String collectionId,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {

        MappedCollection mappedCollection;
        try {
            mappedCollection = mappedCollectionService.findOrAddMappedCollectionByCollectionId(collectionId);
            if (mappedCollection == null) {
                return HttpStatus.NOT_FOUND;
            }
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            return HttpStatus.NOT_FOUND;
        }

        if (quadruple == null) {
            return HttpStatus.NO_CONTENT;
        }

        String token = getTokenFromHeader(authHeader);
        TokenInfo tokenInfo = null;
        try {
            if (token != null) {
                tokenInfo = citesphereConnectorImpl.getTokenInfo(token);
            }
            Collection collection = collectionManager.findCollection(collectionId);
            // either token info wasn't returned by citesphere or the token has expired
            if (collection.getApps() != null && !collection.getApps().isEmpty() && (tokenInfo == null
                    || !tokenInfo.isActive() || !collection.getApps().contains(tokenInfo.getClient_id()))) {
                return HttpStatus.UNAUTHORIZED;
            }

        } catch (OAuthException e) {

            // we got unauth twice (using existing access token and re-generated one)
            return HttpStatus.UNAUTHORIZED;
        } catch (BadCredentialsException e) {

            // Token is invalid
            return HttpStatus.FORBIDDEN;
        } catch (InvalidObjectIdException e) {
            // No such collection found
            return HttpStatus.NOT_FOUND;
        }

        // the flow will reach here only when token is present, valid and active

        // save network
        List<CreationEvent> events = networkMapper.mapNetworkToEvents(quadruple.getGraph());
        List<EventGraph> eventGraphs = events.stream().map(e -> new EventGraph(e)).collect(Collectors.toList());
        eventGraphs.forEach(e -> {
            e.setCollectionId(mappedCollection.getCollectionId());
            e.setDefaultMapping(quadruple.getGraph().getMetadata().getDefaultMapping());
        });
        eventGraphService.saveEventGraphs(eventGraphs);

        try {
            mappedTripleService.storeMappedGraph(quadruple.getGraph(), mappedCollection);
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

        if (authHeader == null || authHeader.trim().isEmpty()) {
            return null;
        } else {
            // Trims the string "Bearer " to extract the exact token from the Authorization
            // Header
            token = authHeader.substring(7);
            if (token == null || token.trim().isEmpty()) {
                return null;
            }
        }
        return token;
    }

}