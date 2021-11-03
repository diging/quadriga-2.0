package edu.asu.diging.quadriga.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Controller
public class GetCollectionTriplesApiController {

    @Autowired
    private CitesphereConnector citesphereConnector;

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedCollectionService mappedCollectionService;

    @GetMapping(value = "/api/v1/collection/{collectionId}/triples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Triple>> getTriples(@PathVariable("collectionId") String collectionId,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {

        String token = getTokenFromHeader(authHeader);
        TokenInfo tokenInfo = null;
        try {
            if (token != null) {
                tokenInfo = citesphereConnector.getTokenInfo(token);
            }
            Collection collection = collectionManager.findCollection(collectionId);
            // either token info wasn't returned by citesphere or the token has expired
            if (collection.getApps() != null && !collection.getApps().isEmpty() && (tokenInfo == null
                    || !tokenInfo.isActive() || !collection.getApps().contains(tokenInfo.getClient_id()))) {
                return new ResponseEntity<List<Triple>>(HttpStatus.UNAUTHORIZED);
            }

        } catch (OAuthException e) {
            // we got unauth twice (using existing access token and re-generated one)
            return new ResponseEntity<List<Triple>>(HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            // Token is invalid
            return new ResponseEntity<List<Triple>>(HttpStatus.FORBIDDEN);
        } catch (InvalidObjectIdException e) {
            // No such collection found
            return new ResponseEntity<List<Triple>>(HttpStatus.NOT_FOUND);
        }

        MappedCollection mappedCollection;
        try {
            mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId);
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            return new ResponseEntity<List<Triple>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Triple>>(
                mappedTripleService.getMappedTriples(mappedCollection.get_id().toString()), HttpStatus.OK);
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
