package edu.asu.diging.quadriga.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.citesphere.CitesphereConnector;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Controller
public class GetCollectionsApiController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private CitesphereConnector citesphereConnector;

    @GetMapping("/api/v1/collections")
    ResponseEntity<List<Collection>> getCollections(
            @RequestHeader(name = "Authorization", required = true) String authHeader) {

        String token = getTokenFromHeader(authHeader);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        TokenInfo tokenInfo;
        try {
            tokenInfo = citesphereConnector.getTokenInfo(token);
            // either token info wasn't returned by citesphere or the token has expired
            if (tokenInfo == null || !tokenInfo.isActive()) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(collectionManager.getCollections(tokenInfo.getClient_id()), HttpStatus.OK);
        } catch (OAuthException e) {
            // we got unauth twice (using existing access token and re-generated one)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            // Token is invalid
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
