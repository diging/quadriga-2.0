package edu.asu.diging.quadriga.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.model.TripleElement;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphElements;

@Controller
public class ExploreCollectionController {
    
    @Autowired
    private MappedTripleService mappedTripleService;
    
    @RequestMapping(value = "/auth/collections/{collectionId}/explore/{uri}")
    public String exploreTriples(@PathVariable String collectionId, @PathVariable String uri,  Model model) {
        List<Triple> triples = mappedTripleService.getTriplesByUri(collectionId, uri);
        GraphElements graphElements = GraphUtil.mapToGraph(triples);
        model.addAttribute("elements", graphElements);
        return "auth/exploreCollection";
    }

}
