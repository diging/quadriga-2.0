package edu.asu.diging.quadriga.web;

import java.util.List;

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
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.Triple;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.web.model.GraphElements;

@Controller
public class ExploreCollectionController {

    @Autowired
    private MappedCollectionService mappedCollectionService;

    @Autowired
    private MappedTripleService mappedTripleService;

    @RequestMapping(value = "/auth/collections/{collectionId}/explore")
    public String exploreTriples(@PathVariable String collectionId,
            @RequestParam(value = "uri", required = true) String uri, Model model)
            throws InvalidObjectIdException, CollectionNotFoundException {
        MappedCollection mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId);
        List<Triple> triples = mappedTripleService.getTriplesByUri(mappedCollection.get_id().toString(), uri);
        GraphElements graphElements = GraphUtil.mapToGraph(triples);
        model.addAttribute("elements", graphElements);
        model.addAttribute("collection", collectionId);
        return "auth/exploreCollection";
    }

    @GetMapping(value = "/auth/collections/{collectionId}/graph")
    public ResponseEntity<GraphElements> getGraphForUri(@PathVariable String collectionId,
            @RequestParam(value = "uri", required = true) String uri)
            throws InvalidObjectIdException, CollectionNotFoundException {
        MappedCollection mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId);
        List<Triple> triples = mappedTripleService.getTriplesByUri(mappedCollection.get_id().toString(), uri);
        GraphElements graphElements = GraphUtil.mapToGraph(triples);
        return new ResponseEntity<>(graphElements, HttpStatus.OK);
    }

}
