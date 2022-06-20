package edu.asu.diging.quadriga.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

@Controller
public class CollectionsApiControlller {

    @Autowired
    private CollectionManager collectionManager;

    @InjectToken
    @GetMapping("/api/v1/collections")
    public ResponseEntity<List<Collection>> getCollections(TokenInfo tokenInfo) {
        return new ResponseEntity<>(collectionManager.getCollections(tokenInfo.getClient_id()), HttpStatus.OK);
    }

}
