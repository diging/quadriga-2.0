package edu.asu.diging.quadriga.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.converter.IXmltoObject;
import edu.asu.diging.quadriga.core.mongo.ICreationEventService;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;
import edu.asu.diging.quadriga.model.events.CreationEvent;
import edu.asu.diging.quadriga.service.IRepositoryManager;

@Deprecated
@Service
public class RepositoryManager implements IRepositoryManager {

    @Autowired
    private IXmltoObject xmlToObject;

    @Autowired
    private ICreationEventService elementDao;

    @Override
    public List<String> processXMLandStoretoDb(String xml, String type) throws URISyntaxException, ParserException,
            IOException, ParseException, JSONException, InvalidDataException {

        List<List<CreationEvent>> creationEventList = new ArrayList<List<CreationEvent>>();

        creationEventList = xmlToObject.parseXML(xml);
        List<CreationEvent> flattenedlist = creationEventList.stream().flatMap(List::stream)
                .collect(Collectors.toList());
        elementDao.saveCreationEvents(flattenedlist);

        return creationEventList.stream().flatMap(Collection::stream).map(e -> e.getId()).collect(Collectors.toList());

    }

}
