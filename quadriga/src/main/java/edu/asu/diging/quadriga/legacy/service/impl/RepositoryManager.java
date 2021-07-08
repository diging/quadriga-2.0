package edu.asu.diging.quadriga.legacy.service.impl;

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

import edu.asu.diging.quadriga.core.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.core.exceptions.ParserException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.legacy.converter.IXmltoObject;
import edu.asu.diging.quadriga.legacy.service.IRepositoryManager;

@Deprecated
@Service
public class RepositoryManager implements IRepositoryManager {

    @Autowired
    private IXmltoObject xmlToObject;

    @Autowired
    private EventGraphService elementDao;

    @Override
    public List<String> processXMLandStoretoDb(String xml, String type) throws URISyntaxException, ParserException,
            IOException, ParseException, JSONException, InvalidDataException {

        List<List<CreationEvent>> creationEventList = new ArrayList<List<CreationEvent>>();

        creationEventList = xmlToObject.parseXML(xml);
        List<EventGraph> flattenedlist = creationEventList.stream().flatMap(List::stream)
                .map(event -> new EventGraph(event)).collect(Collectors.toList());
        elementDao.saveEventGraphs(flattenedlist);

        return creationEventList.stream().flatMap(Collection::stream).map(e -> e.getId()).collect(Collectors.toList());

    }

}
