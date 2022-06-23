package edu.asu.diging.quadriga.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.GraphPattern;
import edu.asu.diging.quadriga.core.conceptpower.ConceptpowerConnector;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.core.model.events.pattern.AppellationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.CreationEventPattern;
import edu.asu.diging.quadriga.core.model.events.pattern.RelationEventPattern;
import edu.asu.diging.quadriga.core.service.PatternFinder;
import edu.asu.diging.quadriga.core.service.PatternMapper;

public class PatternFinderImpl implements PatternFinder {

    @Autowired
    private PatternMapper patternMapper;

    @Autowired
    private ConceptpowerConnector conceptpowerConnector;

    @Override
    public List<Graph> findGraphsWithPattern(GraphPattern pattern, EventGraph eventGraph) {
        CreationEventPattern patternRoot = patternMapper.mapPattern(pattern);
        List<Graph> result = new ArrayList<>();
        CreationEvent graphRoot = eventGraph.getRootEvent();

        return result;
    }

    private Graph extractGraph(CreationEvent graphNode, CreationEventPattern patternNode) {

        return null;
    }

    private boolean doesMatchPattern(CreationEvent graphNode, CreationEventPattern patternNode) {
        if (patternNode == null) {
            return true;
        }

        if (graphNode == null) {
            return false;
        }

        if (graphNode instanceof RelationEvent && patternNode instanceof RelationEventPattern) {
            return matchRelationNodes((RelationEvent) graphNode, (RelationEventPattern) patternNode);
        } else if (graphNode instanceof AppellationEvent && patternNode instanceof AppellationEventPattern) {
            return matchAppellationNodes((AppellationEvent) graphNode, (AppellationEventPattern) patternNode);
        }

        return false;
    }

    private boolean matchAppellationNodes(AppellationEvent graphNode, AppellationEventPattern patternNode) {

        if (patternNode.getConceptType() != null && !patternNode.getConceptType().isEmpty() && matchConceptType(
                graphNode.getTerm().getInterpretation().getSourceURI(), patternNode.getConceptType())) {
            return false;
        }

        if (patternNode.getInterpretation() != null && !patternNode.getInterpretation().isEmpty()
                && !patternNode.getInterpretation().equals(graphNode.getTerm().getInterpretation().getSourceURI())) {
            return false;
        }

        return true;
    }

    private boolean matchRelationNodes(RelationEvent graphNode, RelationEventPattern patternNode) {
        return doesMatchPattern(graphNode.getRelation().getObject(), patternNode.getObject())
                && doesMatchPattern(graphNode.getRelation().getSubject(), patternNode.getSubject())
                && doesMatchPattern(graphNode.getRelation().getPredicate(), patternNode.getPredicate());
    }

    private boolean matchConceptType(String graphNodeInterpretation, String patternConceptType) {
        ConceptEntry conceptEntry = conceptpowerConnector.getConceptEntry(graphNodeInterpretation);
        if (conceptEntry != null) {
            return conceptEntry.getType().getUri().equals(patternConceptType);
        }
        ConceptpowerResponse similarEntries = conceptpowerConnector.findConceptEntryEqualTo(graphNodeInterpretation);
        if (similarEntries != null && similarEntries.getConceptEntries() != null
                && !similarEntries.getConceptEntries().isEmpty()) {
            return similarEntries.getConceptEntries().get(0).getType().getUri().equals(patternConceptType);
        }
        return false;
    }

}
