package edu.asu.diging.quadriga.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Controller
public class GetCollectionTriplesApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CitesphereConnector citesphereConnector;

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedCollectionService mappedCollectionService;

    @GetMapping(value = "/api/v1/collection/{collectionId}/triples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTriples(@PathVariable("collectionId") String collectionId,
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        try {

            Collection collection = collectionManager.findCollection(collectionId);
            MappedCollection mappedCollection;

            if (collection.getApps() != null && !collection.getApps().isEmpty()) {

                TokenInfo tokenInfo = citesphereConnector.getTokenInfo(getTokenFromHeader(authHeader));

                // either token info wasn't returned by citesphere or the token has expired
                if (tokenInfo == null || !tokenInfo.isActive()
                        || !collection.getApps().contains(tokenInfo.getClient_id())) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }

            mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);

            return new ResponseEntity<>(mapper.writeValueAsString(
                    mappedTripleService.getMappedTriples(mappedCollection.get_id().toString())), HttpStatus.OK);

        } catch (OAuthException e) {
            // we got unauth twice (using existing access token and re-generated one)
            logger.error("Unable to verify the authorization token");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            // Token is invalid
            logger.error("Invalid token");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            // No such collection found
            logger.error("No collection found for id {}", collectionId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting the response to JSON");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
