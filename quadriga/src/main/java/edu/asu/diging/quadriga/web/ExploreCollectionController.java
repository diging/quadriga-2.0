package edu.asu.diging.quadriga.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElement;

@Controller
public class ExploreCollectionController {
    
    @Autowired
    private MappedTripleService mappedTripleService;
    
    public String exploreTriples(String collectionId, String uri) {
        List<Triple> triples = mappedTripleService.getTriplesByUri(collectionId, uri);
        
    }

}
