package edu.asu.diging.quadriga.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.diging.quadriga.converter.IXmltoObject;
import edu.asu.diging.quadriga.domain.elements.Element;
import edu.asu.diging.quadriga.domain.events.CreationEvent;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;
import edu.asu.diging.quadriga.service.IRepositoryManager;

public class RepositoryManager implements IRepositoryManager {
    
    @Autowired
    private IXmltoObject xmlToObject;
    

    @Override
    public String processXMLandStoretoDb(String xml, String type) throws URISyntaxException, ParserException,
            IOException, ParseException, JSONException, InvalidDataException {

        List<List<Element>> creationEventList = new ArrayList<List<Element>>();
        List<CreationEvent> creationEventListwithID = new ArrayList<CreationEvent>();

        creationEventList = xmlToObject.parseXML(xml);
        
        

//        creationEventListwithID = storeManager.insertIntoDb(creationEventList);
//
//        if (type.equals((JSON))) {
//            return converter.convertToJson(creationEventListwithID);
//        } else {
//            return converter.convertToXML(creationEventListwithID);
        ///}
        return "";
    }

}
