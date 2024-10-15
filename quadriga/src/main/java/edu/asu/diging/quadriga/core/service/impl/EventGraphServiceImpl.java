package edu.asu.diging.quadriga.core.service.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.core.data.EventGraphRepository;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.mongo.EventGraphDao;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.NetworkMapper;

@Service
public class EventGraphServiceImpl implements EventGraphService {

    @Autowired
    private EventGraphRepository repo;
    
    @Autowired
    private NetworkMapper networkMapper;
    
    @Autowired
    private EventGraphDao eventGraphDao;

    
    @Override
    public void saveEventGraphs(List<EventGraph> events) {
        for (EventGraph event : events) {
            event.setCreationTime(OffsetDateTime.now());
            repo.save(event);
        }
    }

    @Override
    public List<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId) {
        return repo.findByCollectionIdOrderByCreationTimeAsc(collectionId).orElse(null);
    }
    
    @Override
    public Page<EventGraph> findAllEventGraphsByCollectionId(ObjectId collectionId, Pageable pageable) {
        return repo.findByCollectionIdOrderByCreationTimeAsc(collectionId, pageable).orElse(null);
    }
    
    @Override
    public EventGraph findLatestEventGraphByCollectionId(ObjectId collectionId) {
        return repo.findFirstByCollectionIdOrderByCreationTimeDesc(collectionId).orElse(null);
    }

    
    @Override
    public long getNumberOfSubmittedNetworks(ObjectId collectionId) {
                
        return eventGraphDao.countEventGraphsByCollectionId(collectionId);
        
    }
    
    @Override
    public void mapNetworkAndSave(Graph graph, String collectionId) {
        List<CreationEvent> events = networkMapper.mapNetworkToEvents(graph);
        List<EventGraph> eventGraphs = events.stream().map(e ->  new EventGraph(e)).collect(Collectors.toList());

        eventGraphs.forEach(e -> {
            e.setCollectionId(new ObjectId(collectionId));
            e.setDefaultMapping(graph.getMetadata().getDefaultMapping());
            e.setContext(graph.getMetadata().getContext());
            /*
             * FIXME:
             * 
             * A new story will later be created to get info about just one app from citesphere using OAuth token.
             * This app's name should be stored in eventGraph instead of the client id
             * Until that story is done, we need to store clientId instead of appName
             * We can't store clientId yet as it depends on story Q20-3
             * After merging story Q20-3, this needs to be changed to tokenInfo.getClientId()
             */
            e.setSubmittingApp("AppName");
        });
        saveEventGraphs(eventGraphs); 
    }
    
}
