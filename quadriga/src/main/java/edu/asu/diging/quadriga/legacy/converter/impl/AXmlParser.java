package edu.asu.diging.quadriga.legacy.converter.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.Namespace;

import edu.asu.diging.quadriga.core.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.core.exceptions.ParserException;
import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.core.model.elements.TermPart;
import edu.asu.diging.quadriga.core.model.elements.TermParts;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
@Deprecated
public abstract class AXmlParser {

    public String checkForSpaces(String value) {
        return (value == null ? null : value.trim());
    }

    public abstract List<List<CreationEvent>> parseXML(String xml)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException;

    public abstract RelationEvent getRelationEvent(Element relationEvent, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException;

    public abstract AppellationEvent getAppellationEvent(Element appellationEvent, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException;

    public abstract Term getTermObject(Element term, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParseException, InvalidDataException;

    public abstract TermParts getTermPartsObject(Element printedRepresentation, Namespace nameSpace,
            List<CreationEvent> referencedObjectList) throws ParseException;

    public abstract Set<TermPart> getTermPartObject(Set<Element> TermPartNodes, Namespace nameSpace,
            List<CreationEvent> referencedObjectList) throws ParseException;

}