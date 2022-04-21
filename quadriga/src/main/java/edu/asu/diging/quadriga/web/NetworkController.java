package edu.asu.diging.quadriga.web;

import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.web.service.GraphCreationService;

@Controller
public class NetworkController {

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private GraphCreationService graphCreationService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{collectionId}/network/")
    public String get(@PathVariable String collectionId,
            @RequestParam(value = "sourceURI", required = true) String sourceURI, Model model) {

        if (!isSourceUriValid(sourceURI)) {
            logger.error("Invalid sourceUri: " + sourceURI);
            return "error404Page";
        }

        List<EventGraph> eventGraphs = eventGraphService.findEventGraphsBySourceURI(sourceURI);

        if (eventGraphs == null) {
            logger.error("No network found for sourceUri: " + sourceURI);
            return "error404Page";
        }

        model.addAttribute("elements", graphCreationService.createGraph(eventGraphs));
        model.addAttribute("sourceURI", sourceURI);
        //TODO: to be uncommented after pulling in story/Q20-19
//        model.addAttribute("creator", eventGraphs.get(0).getContext().getCreator());
        model.addAttribute("appName", eventGraphs.get(0).getSubmittingApp());
        model.addAttribute("creationTime", eventGraphs.get(0).getCreationTime());
        return "auth/displayNetwork";
    }

    private boolean isSourceUriValid(String sourceURI) {
        UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });
        return urlValidator.isValid(sourceURI);
    }

}