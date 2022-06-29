package edu.asu.diging.quadriga.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Controller
public class GetCollectionTriplesApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @InjectToken
    @GetMapping(value = "/api/v1/collection/{collectionId}/triples", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTriples(@PathVariable("collectionId") String collectionId, TokenInfo tokenInfo) {
        try {

            Collection collection = collectionManager.findCollection(collectionId);

            if (tokenInfo == null || collection.getApps() == null || collection.getApps().isEmpty()
                    || !collection.getApps().contains(tokenInfo.getClient_id())) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.get(collectionId,
                    MappedTripleType.DEFAULT_MAPPING);
            if (mappedTripleGroup == null) {
                logger.error(
                        "Couldn't find or persist a new MappedTripleGroup entry for collectionId: " + collectionId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);

            return new ResponseEntity<>(
                    mapper.writeValueAsString(
                            mappedTripleService.getMappedTriples(mappedTripleGroup.get_id().toString())),
                    HttpStatus.OK);

        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("No collection found for id {}", collectionId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting the response to JSON");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
