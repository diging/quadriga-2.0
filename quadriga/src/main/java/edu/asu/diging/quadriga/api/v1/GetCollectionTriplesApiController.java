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
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.core.aspect.annotation.VerifyCollectionAccess;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

@Controller
public class GetCollectionTriplesApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MappedTripleService mappedTripleService;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @InjectToken
    @VerifyCollectionAccess
    @GetMapping(value = "/api/v1/collection/{collectionId}/triples/{tripleGroupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTriples(@PathVariable("collectionId") String collectionId,
            @PathVariable("tripleGroupId") String tripleGroupId,
            @RequestParam(name = "page", required = false, defaultValue = "1") String page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") String pageSize,
            TokenInfo tokenInfo) {

        int pageInt = 1;
        int pageSizeInt = 10;

        try {
            pageInt = Integer.parseInt(page);
            pageSizeInt = Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
            logger.warn("Trying to access invalid page number: " + page);

        }

        MappedTripleGroup mappedTripleGroup;
        try {
            mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndMappingType(collectionId,
                    MappedTripleType.DEFAULT_MAPPING);
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("No collection found for id {}", collectionId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (mappedTripleGroup == null) {
            logger.error("Couldn't find MappedTripleGroup entry for collectionId: " + collectionId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        String response;
        try {
            response = mapper.writeValueAsString(
                    mappedTripleService.getMappedTriples(mappedTripleGroup.get_id().toString(), pageInt, pageSizeInt));
        } catch (JsonProcessingException e) {
            logger.error("Error while converting the response to JSON", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
