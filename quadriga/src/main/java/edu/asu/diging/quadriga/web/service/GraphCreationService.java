package edu.asu.diging.quadriga.web.service;

import java.util.List;


import edu.asu.diging.quadriga.core.model.DefaultMapping;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.web.service.model.GraphElements;

/**
 * This class is used as a service to create a network graph for the Cytoscape JS
 * library. Any class that wants to have a custom implementation of graph creation
 * for CytoscapeJS should implement this service.
 * 
 * @author poojakulkarni
 *
 */
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
     * Mapping triples to a graph
     * @param triples is the list of triples which is used to create edges and nodes of the graph
     * @return GraphElement object which is created using the nodes and edges created
     */
    public GraphElements mapToGraph(List<DefaultMapping> triples);

}
