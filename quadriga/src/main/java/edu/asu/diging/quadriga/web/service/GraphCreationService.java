package edu.asu.diging.quadriga.web.service;

import java.util.List;

import org.bson.types.ObjectId;

import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.web.model.GraphData;
import edu.asu.diging.quadriga.web.model.GraphElements;
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
    public GraphElements createGraph(EventGraph eventGraph);
    
    /**
     * Creates a node that could be a subject, predicate or subject and adds it to
     * the list of graphNodes
     * 
     * @param graphNodes is the list that maintains all nodes created for the graph
     * @param event is the appellationEvent (subject/object/predicate)
     * @param graphNodeType indicates whether the node is of type subject, object or predicat, used to set group in the node
     * @return an objectId of the created node, later to be used to provide as source/target for creating an edge
     */
    public ObjectId createNode(List<GraphData> graphNodes, AppellationEvent event, GraphNodeType graphNodeType);
    
    /**
     * Creates that edge that links the provided source and target using their IDs and it to
     * the list of graphEdges
     * 
     * @param graphEdges is the list that maintains all edges created for the graph
     * @param sourceId is the source node's id to be linked to the target
     * @param targetId is the target node's id to be linked to the source
     */
    public void createEdge(List<GraphData> graphEdges, ObjectId sourceId, ObjectId targetId);
    

}
