package edu.asu.diging.quadriga.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Controller
public class GetCollectionsApiController {

    @Autowired
    private CollectionManager collectionManager;

    @GetMapping("/api/v1/collections")
    public ResponseEntity<List<Collection>> getCollections(
            @RequestHeader(name = "Authorization", required = false) String authHeader, Authentication authentication, TokenInfo tokenInfo) {
        if (!(authentication.getDetails() instanceof TokenInfo)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
//        TokenInfo tokenInfo = (TokenInfo) authentication.getDetails();
        return new ResponseEntity<>(collectionManager.getCollections(tokenInfo.getClient_id()), HttpStatus.OK);
    }

}
