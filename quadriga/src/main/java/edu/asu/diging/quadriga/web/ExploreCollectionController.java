package edu.asu.diging.quadriga.web;





import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.web.service.model.GraphElements;

@Controller
public class ExploreCollectionController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;

    @Autowired
    private MappedTripleService mappedTripleService;
    
  
    @RequestMapping(value = "/auth/collections/{collectionId}/explore")
    public String exploreTriples(@PathVariable String collectionId, Model model) {
        Collection collection;
        try {
            collection = collectionManager.findCollection(collectionId);
        } catch (InvalidObjectIdException e) {
            logger.error("No Collection found for id",e);
            return "error404Page";
        }
        model.addAttribute("collectionName", collection.getName());
        model.addAttribute("collection", collectionId);
        return "auth/exploreCollection";
    }

    @GetMapping(value = "/auth/collections/{collectionId}/sub-graph")
    public ResponseEntity<GraphElements> getGraphForUri(@PathVariable String collectionId,
            @RequestParam(value = "uri", required = true) String uri,
            @RequestParam(value = "ignoreList", required = false, defaultValue = "{}") List<String> ignoreList)
            throws InvalidObjectIdException,CollectionNotFoundException{
        GraphElements graphElements = new GraphElements();
        try {
            MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndMappingType(collectionId, MappedTripleType.DEFAULT_MAPPING);
            List<DefaultMapping> triples= mappedTripleService.getTriplesByUri(mappedTripleGroup.get_id().toString(),
                    uri, ignoreList);
            
            graphElements = GraphUtil.mapToGraph(triples);
        }
        catch(InvalidObjectIdException e)
        {
            logger.error("Invalid Object Id ",e);
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        catch(CollectionNotFoundException e)
        {
            logger.error("No Collection found",e);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(graphElements, HttpStatus.OK);
    }


}
