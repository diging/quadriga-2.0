package edu.asu.diging.quadriga.converter.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.Namespace;

import edu.asu.diging.quadriga.domain.elements.Term;
import edu.asu.diging.quadriga.domain.elements.TermPart;
import edu.asu.diging.quadriga.domain.elements.TermParts;
import edu.asu.diging.quadriga.domain.events.AppellationEvent;
import edu.asu.diging.quadriga.domain.events.RelationEvent;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;


public abstract class AXmlParser {



    public String checkForSpaces(String value) {
        return (value == null ? null : value.trim());
    }

    
    
    public abstract List<List<edu.asu.diging.quadriga.domain.elements.Element>> parseXML(String xml) throws ParserException,
    IOException, URISyntaxException, ParseException,
    InvalidDataException;
    
    public abstract RelationEvent getRelationEvent(Element relationEvent,
            Namespace nameSpace, List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList)
            throws ParserException, IOException, URISyntaxException,
            ParseException, InvalidDataException;
    
    
    
    
    public abstract AppellationEvent getAppellationEvent(Element appellationEvent,
            Namespace nameSpace, List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList)
            throws ParserException, IOException, URISyntaxException,
            ParseException, InvalidDataException;
    

    
    public abstract Term getTermObject(Element term, Namespace nameSpace,
            List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList) throws ParseException, InvalidDataException ;
    
    public abstract TermParts getTermPartsObject(Element printedRepresentation,
            Namespace nameSpace, List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList)
            throws ParseException ;
    

    public abstract Set<TermPart> getTermPartObject(Set<Element> TermPartNodes,
            Namespace nameSpace, List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList)
            throws ParseException;
    
    
    
}