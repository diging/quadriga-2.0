package edu.asu.diging.quadriga.legacy.converter.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.core.exceptions.ParserException;
import edu.asu.diging.quadriga.core.model.elements.Concept;
import edu.asu.diging.quadriga.core.model.elements.Relation;
import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.core.model.elements.TermPart;
import edu.asu.diging.quadriga.core.model.elements.TermParts;
import edu.asu.diging.quadriga.core.model.events.AppellationEvent;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.model.events.RelationEvent;
import edu.asu.diging.quadriga.legacy.converter.IXmlElements;
import edu.asu.diging.quadriga.legacy.converter.IXmltoObject;
import edu.asu.diging.quadriga.legacy.factory.elements.IActorFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.IConceptFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.IPlaceFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.IRelationFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.ISourceReferenceFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermPartFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermPartsFactory;
import edu.asu.diging.quadriga.legacy.factory.elements.IVocabularyEntryFactory;
import edu.asu.diging.quadriga.legacy.factory.events.IAppellationEventFactory;
import edu.asu.diging.quadriga.legacy.factory.events.IRelationEventFactory;
@Deprecated
@Service
public class XmlToObject extends AXmlParser implements IXmltoObject {

    @Autowired
    private IRelationEventFactory relationEventFactory;

    @Autowired
    private IAppellationEventFactory appellationEventFactory;

    @Autowired
    private ITermPartsFactory termPartsFactory;

    @Autowired
    private IRelationFactory relationFactory;
    @Autowired
    private IActorFactory actorFactory;

    @Autowired
    private IPlaceFactory placeFactory;

    @Autowired
    private ISourceReferenceFactory sourceReferenceFactory;

    @Autowired
    private ITermFactory termFactory;

    @Autowired
    private IConceptFactory conceptFactory;

    @Autowired
    private IVocabularyEntryFactory vocabularyEntryFactory;

    @Autowired
    private ITermPartFactory termPartFactory;

    /**
     * This method parses the XML file and creates the Objects for storing in the
     * Database.
     * 
     * @param xml Input file given by the user in form of string.
     * @throws InvalidDataException
     * @returns list of creation events present in the input string.
     */

    public List<List<CreationEvent>> parseXML(String xml)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException {

        SAXBuilder XMLbuilder = new SAXBuilder();
        Document XMLDocument;

        try {
            String formattedXML = Format.compact(xml);
            XMLDocument = (Document) XMLbuilder.build((new StringReader(formattedXML)));

        } catch (JDOMException e) {
            throw new ParserException(e.toString(), e);
        }

        Element rootElement = XMLDocument.getRootElement();
        Namespace nameSpace = rootElement.getNamespace();
        List<Element> Children = rootElement.getChildren();
        Iterator<Element> childrenIterator = Children.iterator();
        List<List<CreationEvent>> listOfObjects = new ArrayList<List<CreationEvent>>();

        List<CreationEvent> creationEventList = new ArrayList<CreationEvent>();
        List<CreationEvent> referencedObjectList = new ArrayList<CreationEvent>();

        do {
            Element childNode = childrenIterator.next();
            if (childNode.getName().equals(IXmlElements.APPELLATION_EVENT)) {
                AppellationEvent rootNode = getAppellationEvent(childNode, nameSpace, referencedObjectList);
                creationEventList.add(rootNode);

            }

            else if (childNode.getName().equals(IXmlElements.RELATION_EVENT))

            {

                RelationEvent rootNode = getRelationEvent(childNode, nameSpace, referencedObjectList);
                creationEventList.add(rootNode);

            } else {
                throw new InvalidDataException();
            }
        } while (childrenIterator.hasNext());

        listOfObjects.add(creationEventList);
        listOfObjects.add(referencedObjectList);
        return listOfObjects;
    }

    /**
     * This method creates objects for the relational event present in the input
     * string.
     * 
     * @param relationEvent Element object from the input string which is a
     *                      relational event.
     * @return RelationEvent object.
     * @throws InvalidDataException
     */

    public RelationEvent getRelationEvent(Element relationEvent, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException {

        DateFormat formatter = new SimpleDateFormat(IXmlElements.DATE_FORMAT);

        RelationEvent relationEventObject = relationEventFactory.createRelationEvent();

        createRelationEvent(relationEventObject, relationEvent, nameSpace, referencedObjectList, formatter);
        if (relationEventObject.getRefId() != null) {
            referencedObjectList.add(relationEventObject);
        }
        return relationEventObject;
    }

    private void createRelationEvent(RelationEvent relationEventObject, Element relationEvent, Namespace nameSpace,
            List<CreationEvent> referencedObjectList, DateFormat formatter)
            throws ParseException, ParserException, IOException, URISyntaxException, InvalidDataException {

        relationEventObject.setId(checkForSpaces(relationEvent.getChildText(IXmlElements.ID, nameSpace)));

        relationEventObject.setRefId(checkForSpaces(relationEvent.getChildText(IXmlElements.REFID, nameSpace)));
        relationEventObject.setCreator(
                actorFactory.createActor(checkForSpaces(relationEvent.getChildText(IXmlElements.CREATOR, nameSpace))));

//        if (checkForSpaces(relationEvent.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {
//            relationEventObject.setCreationDate((Date) formatter
//                    .parse(checkForSpaces(relationEvent.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//        }

        relationEventObject.setCreationPlace(placeFactory
                .createPlace(checkForSpaces(relationEvent.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));
        relationEventObject.setRelationCreator(actorFactory
                .createActor(checkForSpaces(relationEvent.getChildText(IXmlElements.RELATION_CREATOR, nameSpace))));
        relationEventObject.setSourceReference(sourceReferenceFactory.createSourceReference(
                checkForSpaces(relationEvent.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));
        relationEventObject.setInterpretationCreator(actorFactory.createActor(
                checkForSpaces(relationEvent.getChildText(IXmlElements.INTERPRETATION_CREATOR, nameSpace))));

        Element relationChild = relationEvent.getChild(IXmlElements.RELATION, nameSpace);

        Relation relationObject = relationFactory.createRelation();

        relationObject.setRefId(checkForSpaces(relationChild.getChildText(IXmlElements.REFID, nameSpace)));

        relationObject.setId(checkForSpaces(relationChild.getChildText(IXmlElements.ID, nameSpace)));

        relationObject.setCreator(
                actorFactory.createActor(checkForSpaces(relationChild.getChildText(IXmlElements.CREATOR, nameSpace))));
//        if (checkForSpaces(relationChild.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {
//            relationObject.setCreationDate((Date) formatter
//                    .parse(checkForSpaces(relationChild.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//        }
        relationObject.setCreationPlace(placeFactory
                .createPlace(checkForSpaces(relationChild.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));
        relationObject.setSourceReference(sourceReferenceFactory.createSourceReference(
                checkForSpaces(relationChild.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));

        Element subjectChild = relationChild.getChild(IXmlElements.SUBJECT, nameSpace);

        if (subjectChild != null) {

            createAppellationEvent(subjectChild, relationObject, nameSpace, referencedObjectList);

        }

        else {
            throw new InvalidDataException("subject for Relation is missing");
        }
        Element predicateChild = relationChild.getChild(IXmlElements.PREDICATE, nameSpace);

        if (predicateChild != null) {

            {
                Element appellationRelation = predicateChild.getChild(IXmlElements.APPELLATION_EVENT, nameSpace);
                AppellationEvent appellationPredEventObject = getAppellationEvent(appellationRelation, nameSpace,
                        referencedObjectList);
                relationObject.setPredicate(appellationPredEventObject);
            }
        } else {
            throw new InvalidDataException("predicate for Relation is missing");
        }

        Element objectChild = relationChild.getChild(IXmlElements.OBJECT, nameSpace);

        if (objectChild != null)

        {
            List<Element> object = objectChild.getChildren();
            Iterator<Element> objectIterator = object.iterator();
            Element appellationRelation = objectIterator.next();

            if (checkForSpaces(appellationRelation.getName()).equals(IXmlElements.APPELLATION_EVENT)) {
                AppellationEvent appellationObjEventObject = getAppellationEvent(appellationRelation, nameSpace,
                        referencedObjectList);
                relationObject.setObject(appellationObjEventObject);
            } else if (checkForSpaces(appellationRelation.getName()).equals(IXmlElements.RELATION_EVENT)) {
                RelationEvent childRelationEventObject = relationEventFactory.createRelationEvent();
                childRelationEventObject = getRelationEvent(appellationRelation, nameSpace, referencedObjectList);
                relationObject.setObject(childRelationEventObject);

            }
        } else {
            throw new InvalidDataException("predicate for Relation is missing");
        }

        if (relationObject.getRefId() != null) {
          //  referencedObjectList.add(relationObject);
        }

        relationEventObject.setRelation(relationObject);

    }

    private void createAppellationEvent(Element subjectChild, Relation relationObject, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException {
        List<Element> subject = subjectChild.getChildren();
        Iterator<Element> subjectIterator = subject.iterator();
        Element appellationRelation = subjectIterator.next();

        if (appellationRelation.getName().equals(IXmlElements.APPELLATION_EVENT)) {
            AppellationEvent appellationSubEventObject = getAppellationEvent(appellationRelation, nameSpace,
                    referencedObjectList);
            relationObject.setSubject(appellationSubEventObject);

        } else if (appellationRelation.getName().equals(IXmlElements.RELATION_EVENT)) {
            RelationEvent childRelationEventObject = relationEventFactory.createRelationEvent();
            childRelationEventObject = getRelationEvent(appellationRelation, nameSpace, referencedObjectList);
            relationObject.setSubject(childRelationEventObject);

        }

    }

    /**
     * This method creates objects for the appellation event present in the input
     * string.
     * 
     * @param appellationEvent Element object from the input string which is an
     *                         appellation event.
     * @return AppellationEvent object.
     * @throws InvalidDataException
     */

    public AppellationEvent getAppellationEvent(Element appellationEvent, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException {

        DateFormat formatter;
        formatter = new SimpleDateFormat(IXmlElements.DATE_FORMAT);
        AppellationEvent appellationEventObject = appellationEventFactory.createAppellationEvent();

        appellationEventObject.setId(checkForSpaces(appellationEvent.getChildText(IXmlElements.ID, nameSpace)));

        appellationEventObject.setRefId(checkForSpaces(appellationEvent.getChildText(IXmlElements.REFID, nameSpace)));
        appellationEventObject.setCreator(actorFactory
                .createActor(checkForSpaces(appellationEvent.getChildText(IXmlElements.CREATOR, nameSpace))));
//        if (checkForSpaces(appellationEvent.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {
//
//            appellationEventObject.setCreationDate((Date) formatter
//                    .parse(checkForSpaces(appellationEvent.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//        }
        appellationEventObject.setCreationPlace(placeFactory
                .createPlace(checkForSpaces(appellationEvent.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));
        appellationEventObject.setInterpretationCreator(actorFactory.createActor(
                checkForSpaces(appellationEvent.getChildText(IXmlElements.INTERPRETATION_CREATOR, nameSpace))));
        appellationEventObject.setSourceReference(sourceReferenceFactory.createSourceReference(
                checkForSpaces(appellationEvent.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));

        Element term = (Element) appellationEvent.getChild(IXmlElements.TERM, nameSpace);

        if (term != null) {
            Term termObject = getTermObject(term, nameSpace, referencedObjectList);

            appellationEventObject.setTerm(termObject);
        } else {
            throw new InvalidDataException("term for appellation is missing");
        }

        if (appellationEventObject.getRefId() != null) {
            referencedObjectList.add(appellationEventObject);
        }
        return appellationEventObject;
    }

    /**
     * This method creates object for the Term present in the appellation Event.
     * 
     * @param term                 Element Term object from the input string which
     *                             is in an appellation event.
     * @param nameSpace            NameSpace of the input XML.
     * @param referencedObjectList List containing the referenced Objects
     * @return Term object.
     * @throws InvalidDataException
     */
    public Term getTermObject(Element term, Namespace nameSpace,
            List<CreationEvent> referencedObjectList)
            throws ParseException, InvalidDataException {
        DateFormat formatter;
        formatter = new SimpleDateFormat(IXmlElements.DATE_FORMAT);
        Term termObject = termFactory.createTerm();

        termObject.setRefId(checkForSpaces(term.getChildText(IXmlElements.REFID, nameSpace)));

        termObject.setId(checkForSpaces(term.getChildText(IXmlElements.ID, nameSpace)));
        termObject.setCreator(
                actorFactory.createActor(checkForSpaces(term.getChildText(IXmlElements.CREATOR, nameSpace))));
        termObject.setCreationPlace(
                placeFactory.createPlace(checkForSpaces(term.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));

//        if (checkForSpaces(term.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {
//
//            termObject.setCreationDate(
//                    (Date) formatter.parse(checkForSpaces(term.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//        }

        // set term with interpretation and datatype
        Element interpretationElement = term.getChild(IXmlElements.INTERPRETATION, nameSpace);
        if (interpretationElement != null) {
            String interpretationValue = interpretationElement.getText();
            String datatype = interpretationElement.getAttributeValue(IXmlElements.INTERPRETATION_DATATYPE);
            Concept concept = conceptFactory.createConcept(checkForSpaces(interpretationValue));
            termObject.setInterpretation(concept);
            if (datatype != null) {
                termObject.setDatatype(datatype);
            }
        }

        // set source reference
        termObject.setSourceReference(sourceReferenceFactory
                .createSourceReference(checkForSpaces(term.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));
        termObject.setIsCertain(new Boolean(checkForSpaces(term.getChildText(IXmlElements.CERTAIN, nameSpace))));
        termObject.setNormalizedRepresentation(vocabularyEntryFactory.createVocabularyEntry(
                checkForSpaces(term.getChildText(IXmlElements.NORMALIZED_REPRESENTATION, nameSpace))));

        Element printedRepresentation = (Element) term.getChild(IXmlElements.PRINTED_REPRESENTATION, nameSpace);

        if (printedRepresentation != null) {

            TermParts termPartsObject = getTermPartsObject(printedRepresentation, nameSpace, referencedObjectList);

            termObject.setPrintedRepresentation(termPartsObject);
        }
        Element refTermsElement = (Element) term.getChild(IXmlElements.REFERENCED_TERMS, nameSpace);
        if (refTermsElement != null) {
            List<Element> refTermsArrayList = refTermsElement.getChildren(IXmlElements.TERM, nameSpace);
            Set<Element> refTermsList = new HashSet<Element>(refTermsArrayList);
            Iterator<Element> refTermIterator = refTermsList.iterator();
            Set<Term> referencedTerms = new HashSet<Term>();

            while (refTermIterator.hasNext()) {
                Element refTerm = refTermIterator.next();
                Term refTermObject = termFactory.createTerm();

                refTermObject = getTermObject(refTerm, nameSpace, referencedObjectList);
                referencedTerms.add(refTermObject);

            }

            termObject.setReferencedTerms(referencedTerms);
        }

        if (termObject.getRefId() != null) {
           // referencedObjectList.add(termObject);
        }
        return termObject;
    }

    /**
     * This method creates object for the TermParts present in the Term Element.
     * 
     * @param printedRepresentation TermParts object from the input string which is
     *                              in an Term element.
     * @param nameSpace             NameSpace of the input XML.
     * @param referencedObjectList  List containing the referenced Objects
     * @return TermParts object.
     */
    public TermParts getTermPartsObject(Element printedRepresentation, Namespace nameSpace,
            List<CreationEvent> referencedObjectList) throws ParseException {
        DateFormat formatter;
        formatter = new SimpleDateFormat(IXmlElements.DATE_FORMAT);
        TermParts termPartsObject = termPartsFactory.createTermParts();

        termPartsObject.setRefId(checkForSpaces(printedRepresentation.getChildText(IXmlElements.REFID, nameSpace)));
        termPartsObject.setId(checkForSpaces(printedRepresentation.getChildText(IXmlElements.ID, nameSpace)));
        termPartsObject.setCreator(actorFactory
                .createActor(checkForSpaces(printedRepresentation.getChildText(IXmlElements.CREATOR, nameSpace))));
        termPartsObject.setCreationPlace(placeFactory.createPlace(
                checkForSpaces(printedRepresentation.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));

//        if (checkForSpaces(printedRepresentation.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {

//            termPartsObject.setCreationDate((Date) formatter
//                    .parse(checkForSpaces(printedRepresentation.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//        }
        termPartsObject.setReferencedSource(sourceReferenceFactory.createSourceReference(
                checkForSpaces(printedRepresentation.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));

        List<Element> TermPartNodesArrayList = printedRepresentation.getChildren(IXmlElements.TERM_PART, nameSpace);
        Set<Element> TermPartNodes = new HashSet<Element>(TermPartNodesArrayList);
        Set<TermPart> termPartList = getTermPartObject(TermPartNodes, nameSpace, referencedObjectList);

        termPartsObject.setTermParts(termPartList);
        if (checkForSpaces(termPartsObject.getRefId()) != null) {
          //  referencedObjectList.add(termPartsObject);
        }

        return termPartsObject;
    }

    /**
     * This method creates list of TermPart objects present in the TermParts
     * Element.
     * 
     * @param TermPartNodes        List of TermPart elements from the input string
     *                             which is in an TermParts element.
     * @param nameSpace            NameSpace of the input XML.
     * @param referencedObjectList List containing the referenced Objects
     * @return List of TermPart object.
     */
    @Override
    public Set<TermPart> getTermPartObject(Set<Element> TermPartNodes, Namespace nameSpace,
            List<CreationEvent> referencedObjectList) throws ParseException {
        DateFormat formatter;
        formatter = new SimpleDateFormat(IXmlElements.DATE_FORMAT);

        Iterator<Element> TermPartIterator = TermPartNodes.iterator();
        Set<TermPart> termPartList = new HashSet<TermPart>();

        while (TermPartIterator.hasNext()) {
            TermPart termPartObject = termPartFactory.createTermPart();
            Element currentElement = (Element) TermPartIterator.next();

            termPartObject.setRefId(checkForSpaces(currentElement.getChildText(IXmlElements.REFID, nameSpace)));
            termPartObject.setId(checkForSpaces(currentElement.getChildText(IXmlElements.ID, nameSpace)));
            termPartObject.setCreator(actorFactory
                    .createActor(checkForSpaces(currentElement.getChildText(IXmlElements.CREATOR, nameSpace))));
            termPartObject.setCreationPlace(placeFactory
                    .createPlace(checkForSpaces(currentElement.getChildText(IXmlElements.CREATION_PLACE, nameSpace))));
//            if (checkForSpaces(currentElement.getChildText(IXmlElements.CREATION_DATE, nameSpace)) != null) {
//
//                termPartObject.setCreationDate((Date) formatter
//                        .parse(checkForSpaces(currentElement.getChildText(IXmlElements.CREATION_DATE, nameSpace))));
//            }
            termPartObject.setSourceReference(sourceReferenceFactory.createSourceReference(
                    checkForSpaces(currentElement.getChildText(IXmlElements.SOURCE_REFERENCE, nameSpace))));
            termPartObject
                    .setExpression(checkForSpaces(currentElement.getChildText(IXmlElements.EXPRESSION, nameSpace)));
            termPartObject.setNormalization(vocabularyEntryFactory.createVocabularyEntry(
                    checkForSpaces(currentElement.getChildText(IXmlElements.NORMALIZATION, nameSpace))));
            String position = checkForSpaces(currentElement.getChildText(IXmlElements.POSITION, nameSpace));
//            if (position != null && !position.isEmpty()) {
//                termPartObject.setPosition(Integer.parseInt(position));
//            }
            termPartObject.setFormat(checkForSpaces(currentElement.getChildText(IXmlElements.FORMAT, nameSpace)));
            termPartObject.setFormattedPointer(
                    checkForSpaces(currentElement.getChildText(IXmlElements.FORMATTED_POINTER, nameSpace)));
            if (termPartObject.getRefId() != null) {
               // referencedObjectList.add(termPartObject);
            }
            termPartList.add(termPartObject);

        }
        return termPartList;
    }
}