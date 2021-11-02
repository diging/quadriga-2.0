package edu.asu.diging.quadriga.web.service;

import java.util.List;
import java.util.Map;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElements;
import edu.asu.diging.quadriga.web.model.GraphNodeData;
import edu.asu.diging.quadriga.web.model.GraphNodeType;

public interface GraphCreationService {
    
    /**
     * Creates a graph in the form that could be understood by Cytoscape:
     * 
     * elements: {
     *      nodes: {
     *          [
     *              {data: {id: "", group: "", label: ""}},
     *              {data: {id: "", group: "", label: ""}}
     *          ]
     *      },
     *      edges: {
     *          [
     *              {data: {id: "", source: "", target: ""}},
     *              {data: {id: "", source: "", target: ""}},
     *              {data: {id: "", source: "", target: ""}} 
     *          ]
     *      }
     * }
     * 
     * @param eventGraph is the source of data for creating the graph
     * @return a GraphElements object that resembles the above JSON structure
     */
    public GraphElements createGraph(List<EventGraph> eventGraph);
    
    public String createPredicateNode(List<GraphData> graphNodes, AppellationEvent event);
    
    public String createSubjectOrObjectNode(List<GraphData> graphNodes, AppellationEvent event, Map<String, GraphNodeData> uniqueNodes, GraphNodeType graphNodeType);
    
    public GraphNodeData createNode(AppellationEvent event, GraphNodeType graphNodeType);
    
    /**
     * Creates that edge that links the provided source and target using their IDs and it to
     * the list of graphEdges
     * 
     * @param graphEdges is the list that maintains all edges created for the graph
     * @param sourceId is the source node's id to be linked to the target
     * @param targetId is the target node's id to be linked to the source
     */
    public void createEdge(List<GraphData> graphEdges, String sourceId, String targetId);
    

}
