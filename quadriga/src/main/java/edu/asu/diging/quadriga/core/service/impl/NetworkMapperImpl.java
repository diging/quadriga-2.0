package edu.asu.diging.quadriga.core.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.api.v1.model.Edge;
import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.NetworkConstants;
import edu.asu.diging.quadriga.api.v1.model.NodeData;
import edu.asu.diging.quadriga.api.v1.model.NodeMetadata;
import edu.asu.diging.quadriga.core.model.elements.Actor;
import edu.asu.diging.quadriga.core.model.elements.Concept;
import edu.asu.diging.quadriga.core.model.elements.Place;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.core.model.elements.TermPart;
import edu.asu.diging.quadriga.core.model.elements.TermParts;
import edu.asu.diging.quadriga.core.model.elements.VocabularyEntry;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.service.NetworkMapper;
import edu.asu.diging.quadriga.core.service.TriFunction;

@Service
public class NetworkMapperImpl implements NetworkMapper {

    private Map<String, TriFunction<String, NodeData, Graph, CreationEvent>> creationMethods;

    public NetworkMapperImpl() {
        creationMethods = new HashMap<>();
        creationMethods.put(NetworkConstants.APPELLATION_EVENT_TYPE, this::createAppellationEvent);
        creationMethods.put(NetworkConstants.RELATION_EVENT_TYPE, this::createRelationEvent);
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.impl.NetworkMapper#mapNetworkToEvents(edu.asu.diging.quadriga.api.v1.model.Graph)
     */
    @Override
    public List<CreationEvent> mapNetworkToEvents(Graph graph) {
        List<Entry<String, NodeData>> startNodes = graph.getNodes().entrySet().stream()
                .filter(node -> graph.getEdges().stream().noneMatch(edge -> edge.getTarget().equals(node.getKey())))
                .collect(Collectors.toList());

        List<CreationEvent> events = new ArrayList<>();
        for (Entry<String, NodeData> node : startNodes) {
            events.add(createEvent(node.getKey(), node.getValue(), graph));
        }
        
        return events;
    }

    private CreationEvent createEvent(String nodeId, NodeData node, Graph graph) {
        NodeMetadata data = node.getMetadata();
        TriFunction<String, NodeData, Graph, CreationEvent> method = creationMethods.get(data.getType());
        return method.apply(nodeId, node, graph);
    }

    private CreationEvent createRelationEvent(String nodeId, NodeData node, Graph graph) {
        RelationEvent relationEvent = new RelationEvent();
        setContextData(node, relationEvent);

        Relation relation = new Relation();
        relationEvent.setRelation(relation);

        Optional<Edge> subject = graph.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_SUBJECT))
                .findFirst();
        if (subject.isPresent()) {
            relation.setSubject(
                    createEvent(subject.get().getTarget(), graph.getNodes().get(subject.get().getTarget()), graph));
        }

        Optional<Edge> predicate = graph.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_PREDICATE))
                .findFirst();
        if (predicate.isPresent()) {
            relation.setPredicate((AppellationEvent)
                    createEvent(predicate.get().getTarget(), graph.getNodes().get(predicate.get().getTarget()), graph));
        }

        Optional<Edge> object = graph.getEdges().stream().filter(
                e -> e.getSource().equals(nodeId) && e.getRelation().equals(NetworkConstants.RELATION_TYPE_OBJECT))
                .findFirst();
        if (object.isPresent()) {
            relation.setObject(
                    createEvent(object.get().getTarget(), graph.getNodes().get(object.get().getTarget()), graph));
        }
        
        return relationEvent;
    }

    private CreationEvent createAppellationEvent(String nodeId, NodeData node, Graph graph) {
        AppellationEvent appellationEvent = new AppellationEvent();
        setContextData(node, appellationEvent);

        Concept concept = new Concept();
        concept.setSourceURI(node.getMetadata().getInterpretation());

        Term term = new Term();
        term.setInterpretation(concept);

        term.setPrintedRepresentation(new TermParts());
        term.getPrintedRepresentation().setTermParts(new HashSet<>());
        for (edu.asu.diging.quadriga.api.v1.model.TermPart part : node.getMetadata().getTermParts()) {
            TermPart termPart = new TermPart();
            termPart.setExpression(part.getExpression());
            termPart.setFormat(part.getFormat());
            termPart.setFormattedPointer(part.getFormattedPointer());
            termPart.setPosition(part.getPosition());

            VocabularyEntry entry = new VocabularyEntry();
            entry.setTerm(part.getExpression());
            termPart.setNormalization(entry);
            term.getPrintedRepresentation().getTermParts().add(termPart);
        }
        appellationEvent.setTerm(term);

        return appellationEvent;
    }

    private void setContextData(NodeData node, CreationEvent event) {
        if (node.getContext() == null) {
            return;
        }
        
        event.setCreationDate(node.getContext().getCreationTime());

        Place place = new Place();
        place.setSourceURI(node.getContext().getCreationPlace());
        event.setCreationPlace(place);

        Actor actor = new Actor();
        actor.setSourceURI(node.getContext().getCreator());
        event.setCreator(actor);
    }
}
