package edu.asu.diging.quadriga.web.service;

import java.util.List;
import java.util.Map;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
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
    
    /**
     * This method creates nodes and edges for subject, object and predicate nodes
     * For a predicate node, the event type is always appellation event and the predicate node is created
     * 
     * For subject and object, if the event type is appellation then the nodes that are created are leaf
     * nodes
     * But if event type is relation event, this method is called recursively to because every relation event
     * itself has subject, object and predicate
     * 
     * After the nodes are created, both subject and object are linked to the predicate node by creating edges
     * One edge goes from subject to predicate, another one from object to predicate
     * 
     * The predicate node's id is returned to the parent of the subtree, so that it could
     * be linked to its parent's predicate
     * 
     * @param event is the relation event for which the method createes subject, object and predicate nodes
     * @param graphNodes maintain the list of nodes created
     * @param graphEdges maintain the list of edges created
     * @param uniqueNodes maintains a map that links every unique sourceURI to its corresponding node
     * @param eventGraphId is the id of the current EventGraph
     * @return
     */
    public String createNodesAndEdges(RelationEvent event, List<GraphData> graphNodes, List<GraphData> graphEdges,
            Map<String, GraphNodeData> uniqueNodes, String eventGraphId);
    
    /**
     * Creates a predicate node using the event data and adds it to list of graph nodes
     * 
     * @param graphNodes list to maintain all current graph nodes
     * @param event is an appellation event that contains data to be set to the node
     * @return the id of the created predicate node
     */
    public String createPredicateNode(List<GraphData> graphNodes, AppellationEvent event);
    
    /**
     * Checks if the unique nodes map contains the same sourceURI as the node to be created
     * If yes, returns this node
     * Else it creates a subject or object node using the event data and adds it to the list of graph nodes
     * and the map of unique nodes with key as sourceURI and value as the node object itself 
     * 
     * @param graphNodes list to maintain all current graph nodes
     * @param event is an appellation event that contains data to be set to the node
     * @param uniqueNodes map to maintain nodes with unique sourceURI that can be reused
     * @param graphNodeType indicates whether node to be created is subject or object node to accordingly set group id
     * @return the id of an existing node from unique nodes or the newly created node
     */
    
    public String createSubjectOrObjectNode(List<GraphData> graphNodes, AppellationEvent event, Map<String, GraphNodeData> uniqueNodes, GraphNodeType graphNodeType);
    
    /**
     * Creates a GraphNodeData object and sets details such as id, label, group
     * 
     * @param event is the appellation event for which node is being created
     * @param graphNodeType used to set group id
     * @return the created GraphNodeData object
     */
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
