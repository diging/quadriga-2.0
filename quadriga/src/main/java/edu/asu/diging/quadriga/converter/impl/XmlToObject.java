package edu.asu.diging.quadriga.converter.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Namespace;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.converter.IXmltoObject;
import edu.asu.diging.quadriga.domain.elements.Element;
import edu.asu.diging.quadriga.domain.events.AppellationEvent;
import edu.asu.diging.quadriga.domain.events.RelationEvent;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;


@Service
public class XmlToObject extends AXmlParser implements IXmltoObject {

    /**
     * This method parses the XML file and creates the Objects for storing in
     * the Database.
     * 
     * @param xml
     *            Input file given by the user in form of string.
     * @throws InvalidDataException
     * @returns list of creation events present in the input string.
     */

    public List<List<edu.asu.diging.quadriga.domain.elements.Element>> parseXML(
            String xml) throws ParserException, IOException,
            URISyntaxException, ParseException, InvalidDataException {

        SAXBuilder XMLbuilder = new SAXBuilder();
        Document XMLDocument;

        try {
            String formattedXML = Format.compact(xml);
            XMLDocument = (Document) XMLbuilder.build((new StringReader(
                    formattedXML)));

        } catch (JDOMException e) {
            throw new ParserException(e.toString(), e);
        }

        Element rootElement = XMLDocument.getRootElement();
        Namespace nameSpace = rootElement.getNamespace();
        List<Element> Children = rootElement.getChildren();
        Iterator<Element> childrenIterator = Children.iterator();
        List<List<edu.asu.diging.quadriga.domain.elements.Element>> listOfObjects = new ArrayList<List<edu.asu.diging.quadriga.domain.elements.Element>>();

        List<edu.asu.diging.quadriga.domain.elements.Element> creationEventList = new ArrayList<edu.asu.diging.quadriga.domain.elements.Element>();
        List<edu.asu.diging.quadriga.domain.elements.Element> referencedObjectList = new ArrayList<edu.asu.diging.quadriga.domain.elements.Element>();

        do {
            Element childNode = childrenIterator.next();
            if (childNode.getName().equals(IXmlElements.APPELLATION_EVENT)) {
                AppellationEvent rootNode = getAppellationEvent(childNode,
                        nameSpace, referencedObjectList);
                creationEventList.add(rootNode);

            }

            else if (childNode.getName().equals(IXmlElements.RELATION_EVENT))

            {

                RelationEvent rootNode = getRelationEvent(childNode, nameSpace,
                        referencedObjectList);
                creationEventList.add(rootNode);

            } else {
                throw new InvalidDataException();
            }
        } while (childrenIterator.hasNext());

        listOfObjects.add(creationEventList);
        listOfObjects.add(referencedObjectList);
        return listOfObjects;
    }

  
}
