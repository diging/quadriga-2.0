package edu.asu.diging.quadriga.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.service.impl.EventGraphServiceImpl;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElement;
import edu.asu.diging.quadriga.web.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;

@Controller
public class NetworkController {
    
    @Autowired
    private EventGraphServiceImpl eventGraphServiceImpl;
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @RequestMapping(value = "/auth/collections/{collectionId}/network/{networkId}")
    public String get(@PathVariable String collectionId, @PathVariable String networkId, Model model) throws JsonProcessingException {
        
        EventGraph eventGraph;
        try {
            eventGraph = eventGraphServiceImpl.findEventGraphById(networkId);
        } catch (InvalidObjectIdException e) {
            logger.error(e.getMessage());
            return "error404Page";
        }
        
        GraphElements graphElements = convertEventGraphToGraphElements(eventGraph);
        if(graphElements == null) {
            model.addAttribute("noNetworkMessage", "No network graph found for this network. This should not happen");
        }
        model.addAttribute("elements", graphElements);
        
        return "auth/displayNetwork";
    }
    
    private List<GraphElement> encloseInGraphData(List<GraphData> dataList) {
        List<GraphElement> elements = new ArrayList<>();
        dataList.forEach(data -> {
            GraphElement element = new GraphElement();
            element.setData(data);
            elements.add(element);
        });
        return elements;
    }
    
    private GraphElements convertEventGraphToGraphElements(EventGraph eventGraph) {
        CreationEvent rootEvent = eventGraph.getRootEvent();
        if(!(rootEvent instanceof RelationEvent)) {
            return null;
        }
        GraphElements graphElements = new GraphElements();
        List<GraphData> graphEdges = new ArrayList<>();
        List<GraphData> graphNodes = new ArrayList<>();
        
        addNodesAndEdges(rootEvent, graphNodes, graphEdges);
        
        graphElements.setNodes(encloseInGraphData(graphNodes));
        graphElements.setEdges(encloseInGraphData(graphEdges));
        return graphElements;
    }

    private void addNodesAndEdges(CreationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges) {
        if(event instanceof RelationEvent) {
            addNodesAndEdges(event, graphNodes, graphEdges);
        }
        // Continue this implementation
    }
    

}