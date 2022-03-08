package edu.asu.diging.quadriga.web;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.MappedTripleGroup;
import edu.asu.diging.quadriga.core.model.MappedTripleType;
import edu.asu.diging.quadriga.core.model.mapped.Predicate;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleGroupService;
import edu.asu.diging.quadriga.core.service.PredicateManager;

@Controller
public class DisplayCollectionController {

    @Autowired
    private CollectionManager collectionManager;

    @Autowired
    private EventGraphService eventGraphService;
    
    @Autowired
    private MappedTripleGroupService mappedTripleGroupService;
    
    @Autowired
    private PredicateManager predicateManager;
    
    @Autowired
    MongoTemplate mongoTemplate;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/auth/collections/{id}", method = RequestMethod.GET)
    public String get(@PathVariable String id, Model model) {
        
        Collection collection;
        try {
            collection = collectionManager.findCollection(id);
            if(collection == null) {
            	logger.error("Couldn't find collection: ", id);
                return "error404Page";
            }
        } catch (InvalidObjectIdException e) {
            return "error404Page";
        }
        
       
        
        List<EventGraph> eventGraphs = eventGraphService.findAllEventGraphsByCollectionId(collection.getId());
        
        if(!eventGraphs.isEmpty()) {
            // We get last network submission info by getting the last EventGraph which will be a part of the last network
            EventGraph lastNetwork = eventGraphs.get(0);
            model.addAttribute("lastNetworkSubmittedAt", lastNetwork.getCreationTime().atZoneSameInstant(ZoneId.systemDefault()));
            model.addAttribute("lastNetworkSubmittedBy", lastNetwork.getSubmittingApp());
        }
        
        model.addAttribute("collectionName", collection.getName());
        model.addAttribute("description", collection.getDescription());
        model.addAttribute("creationTime", collection.getCreationTime().atZoneSameInstant(ZoneId.systemDefault()));
        
        // One network may have multiple eventGraphs, but all of them will have same sourceURI in the context
        // This sourceURI will be used to group eventGraphs together that belong to the same network        
        
        long groupedEventGraphsCount = eventGraphService.groupEventGraphsBySourceUri(collection.getId());
       
        model.addAttribute("numberOfSubmittedNetworks", groupedEventGraphsCount);
        
        // Get default mappings from Concepts
        model.addAttribute("defaultMappings", getNumberOfDefaultMappings(collection.getId().toString()));
        
        return "auth/displayCollection";
        
    }
    
    /**
     * This method returns the number of default mappings present in the collection
     * One MappedTripleGroup will exist for the "DefaultMappings" for this collection
     * To get this number of default mappings, this method will check how many 'Predicates' have
     * this mappedTripleGroupId linked to them
     * This is because every default mapping has one predicate
     * So, if the MappedTripleGroupId is present on n predicates, this collection
     * must have n defaultMappings 
     * 
     * @param collectionId used to find mappedTripleGroupId
     * @return the number of default mappings
     */
    private int getNumberOfDefaultMappings(String collectionId) {
        try {
            MappedTripleGroup mappedTripleGroup = mappedTripleGroupService.findByCollectionIdAndMappingType(collectionId, MappedTripleType.DEFAULT_MAPPING);
            if(mappedTripleGroup != null) {            	
            	
            	return predicateManager.countPredicatesByMappedTripleGroup(mappedTripleGroup.get_id().toString());
            	
            }
            
        } catch (InvalidObjectIdException | CollectionNotFoundException e) {
        	logger.error("Couldn't find number of default mappings for collection: ",collectionId,e.getMessage());
        }
        return 0;
    }

}
