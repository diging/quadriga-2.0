package edu.asu.diging.quadriga.web;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedCollection;
import edu.asu.diging.quadriga.core.model.mapped.Concept;
import edu.asu.diging.quadriga.core.model.mapped.MappingTypes;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.ConceptManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedCollectionService;
import edu.asu.diging.quadriga.web.model.Network;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedCollectionService mappedCollectionService;

    @Autowired
    private ConceptManager conceptManager;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{collectionId}", method = RequestMethod.GET)
    public String get(HttpServletRequest request, @PathVariable String collectionId, Model model) {

        // Get collection details
        Collection collection;
        try {
            collection = collectionManager.findCollection(collectionId);
            if (collection == null) {
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            return "error404Page";
        }

        model.addAttribute("collection", collection);

        // Determin page number and size for network pagination
        int page = 0;
        int size = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
            page = page < 0 ? 0 : page;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
            size = size < 1 ? 10 : size;
        }

        model.addAttribute("size", size);
        
        // Get all EventGraphs for this collection
        List<EventGraph> eventGraphsList = eventGraphService.findAllEventGraphsByCollectionId(collection.getId());

        if (!eventGraphsList.isEmpty()) {
            // Latest EventGraph will be for the last network submitted
            EventGraph lastNetwork = eventGraphService.findLatestEventGraphByCollectionId(collection.getId());
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime());
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getAppName());
            
            // Every EventGraph with same sourceURI belongs to the same network
            // Group all EventGraphs with the same sourceURI together to get all networks
            Map<String, List<EventGraph>> groupedEventGraphs = groupEventGraphs(eventGraphsList);
            List<Network> networks = new ArrayList<>();
            groupedEventGraphs.forEach((sourceURI, eventGraphsInNetwork) -> {
                Network network = new Network();
                network.setSourceURI(sourceURI);
                // The EventGraph that has the oldest creation date & time will be the network's creation date & time
                EventGraph firstEventGraph = Collections.min(eventGraphsInNetwork, (eventGraph1, eventGraph2) -> eventGraph1
                        .getCreationTime().compareTo(eventGraph2.getCreationTime()));
                network.setCreationTime(firstEventGraph.getCreationTime());
                network.setCreator(firstEventGraph.getContext().getCreator());
                network.setAppName(firstEventGraph.getAppName());
                networks.add(network);
            });
            
            // Sort networks as per the creation time
            networks.sort((network1, network2) -> network2.getCreationTime().compareTo(network1.getCreationTime()));
            
            // Add pagination to the networks as per determined page number and size
            model.addAttribute("networks", networks.subList(page * size, Math.min(networks.size(), page * size + size)));
            model.addAttribute("totalPages", networks.size() % 10 == 0 ? (networks.size()/size) : (networks.size()/size + 1));
            model.addAttribute("pageNumber", page);
            model.addAttribute("numberOfSubmittedNetworks", networks.size());
        }

        // Get default mappings from 'Concept' nodes
        model.addAttribute("defaultMappings", getDefaultMappings(collectionId.toString()));

        return "auth/displayCollection";

    }

    /**
     * This method gets a mappedCollection using the current collectionId. Once a
     * mappedCollection is found, it looks for this MappedCollection in 'Concept'
     * nodes. A count of all such concepts with mapping type "DefaultMapping" are
     * returned
     * 
     * Before returning, we divide the count of concepts by two because every
     * default mapping has two concepts, a subject and an object
     * 
     * @param collectionId used to find mappedCollectionId
     * @return the number of default mappings
     */
    private int getDefaultMappings(String collectionId) {
        int defaultMappings = 0;
        MappedCollection mappedCollection = null;
        try {
            mappedCollection = mappedCollectionService.findMappedCollectionByCollectionId(collectionId.toString());
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
            logger.error("Couldn't find mappedCollection to get default mappings from Neo4J for collectionId: "
                    + collectionId);
        }

        if (mappedCollection != null) {
            List<Concept> concepts = conceptManager.findConceptsByMappingTypeAndMappedCollectionId(
                    MappingTypes.DEFAULT_MAPPING, mappedCollection.get_id().toString());
            // We need to divide by two as one default mapping will have 2 concepts, a subject and an object
            defaultMappings = (concepts.isEmpty() ? 0 : concepts.size()) / 2;
        } else {
            logger.error("Couldn't find mappedCollection to get default mappings from Neo4J for collectionId: "
                    + collectionId);
        }
        return defaultMappings;
    }
    
    private Map<String, List<EventGraph>> groupEventGraphs(List<EventGraph> eventGraphs) {
        return eventGraphs.stream().collect(Collectors.groupingBy(eventGraph -> eventGraph.getContext().getSourceUri()));
    }

}
