import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.OffsetDateTime;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import edu.asu.diging.quadriga.api.v1.AddNetworkApiController;
import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Quadruple;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;

class AddNetworkApiControllerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Mock
    private EventGraphService eventGraphService;

    @Mock
    private MappedTripleService mappedTripleService;

    @Mock
    private MappedTripleGroupService mappedTripleGroupService;

    @InjectMocks
    private AddNetworkApiController controller;

    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testProcessJson_Success() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        graph.setTriples(List.of());
        quadruple.setGraph(graph);

        HttpStatus result = controller.processJson(quadruple, "collectionId");

        assertEquals(HttpStatus.ACCEPTED, result);
    }

    @Test
    void testProcessJson_NullQuadruple() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        HttpStatus result = controller.processJson(null, "collectionId");

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    void testProcessJson_NullMappedTripleGroup() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        graph.setTriples(List.of());
        quadruple.setGraph(graph);

        HttpStatus result = controller.processJson(quadruple, "collectionId");

        assertEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    void testProcessJson_StoreMappedGraphThrowsException() throws NodeNotFoundException, InvalidObjectIdException, CollectionNotFoundException {
        Quadruple quadruple = new Quadruple();
        Graph graph = new Graph();
        graph.setTriples(List.of());
        quadruple.setGraph(graph);

        MappedTripleGroup mappedTripleGroup = new MappedTripleGroup();
        mappedTripleGroup.setId(new ObjectId());
        mappedTripleGroup.setCollectionId("collectionId");
        mappedTripleGroup.setType(MappedTripleType.DEFAULT_MAPPING);

        HttpStatus result = controller.processJson(quadruple, "collectionId");

        assertEquals(HttpStatus.BAD_REQUEST, result);
    }

}
