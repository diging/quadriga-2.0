package edu.asu.diging.quadriga.core.service;

import java.util.List;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;

public interface NetworkMapper {

    List<CreationEvent> mapNetworkToEvents(Graph graph);

}