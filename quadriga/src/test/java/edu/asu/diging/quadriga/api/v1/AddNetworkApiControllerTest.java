package edu.asu.diging.quadriga.api.v1;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import edu.asu.diging.quadriga.api.v1.model.Edge;
import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Metadata;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.api.v1.model.Quadruple;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

class AddNetworkApiControllerTest {
    @Mock
    private EventGraphService eventGraphService;

    @Mock
    private MappedTripleService mappedTripleService;

    @Mock
    private MappedTripleGroupService mappedTripleGroupService;

    @InjectMocks
    private AddNetworkApiController addNetworkApiController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);        
    }

    @Test
    void testProcessJson_Success() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        
        ObjectId collectionId = new ObjectId();
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setCollectionId(collectionId);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DEFAULT_MAPPING);
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        Metadata metadata = new Metadata();
        List<Edge> edges = new ArrayList<Edge>();
        Map<String,NodeData> map = new HashMap<String,NodeData>();
        graph.setEdges(edges);
        graph.setMetadata(metadata);
        graph.setNodes(map);
        quadruple.setGraph(graph);
        Mockito.when(mappedTripleGroupService.get(collectionId.toString(),MappedTripleType.DEFAULT_MAPPING)).thenReturn(mappedTripleGroup);
        Mockito.doNothing().when(eventGraphService).mapNetworkAndSave(graph, collectionId.toString());
        Mockito.when(mappedTripleService.storeMappedGraph(quadruple.getGraph(), mappedTripleGroup)).thenReturn(new Predicate());
        
        
        HttpStatus result = addNetworkApiController.processJson(quadruple, collectionId.toString(), null);

        Assert.assertEquals(HttpStatus.ACCEPTED, result);
    }

    @Test
    void testProcessJson_NullQuadruple() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        HttpStatus result = addNetworkApiController.processJson(null, "collectionId", null);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void testProcessJson_NullMappedTripleGroup() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        quadruple.setGraph(graph);

        HttpStatus result = addNetworkApiController.processJson(quadruple, "collectionId", null);

        Assert.assertEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    void testProcessJson_StoreMappedGraphThrowsException() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        quadruple.setGraph(graph);
        
        ObjectId collectionId = new ObjectId();
        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.set_id(new ObjectId());
        mappedTripleGroup.setCollectionId(collectionId);
        mappedTripleGroup.setMappedTripleType(MappedTripleType.DEFAULT_MAPPING);

        HttpStatus result = addNetworkApiController.processJson(quadruple, collectionId.toString(), null);

        Assert.assertEquals(HttpStatus.NOT_FOUND, result);
    }

}
