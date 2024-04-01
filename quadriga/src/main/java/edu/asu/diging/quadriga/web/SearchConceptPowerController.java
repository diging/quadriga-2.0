package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerService;

@Controller
public class SearchConceptPowerController {

    @Autowired
    private ConceptPowerService conceptPowerService;

    @GetMapping("/search/concept")
    public ResponseEntity<ConceptPowerReply> searchConcepts(@RequestParam("searchTerm") String searchTerm,
            @RequestParam(defaultValue = "1", required = false, value = "page") int page) {
        ConceptPowerReply searchResults = conceptPowerService.searchConcepts(searchTerm, page);
        if (searchResults == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

}
