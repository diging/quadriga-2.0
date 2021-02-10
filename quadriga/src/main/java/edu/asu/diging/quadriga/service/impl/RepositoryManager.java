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
import edu.asu.diging.quadriga.core.mongo.ElementDao;
import edu.asu.diging.quadriga.domain.elements.Element;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;
import edu.asu.diging.quadriga.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager {

    @Autowired
    private IXmltoObject xmlToObject;

    @Autowired
    private ElementDao elementDao;

    @Override
    public List<String> processXMLandStoretoDb(String xml, String type) throws URISyntaxException, ParserException,
            IOException, ParseException, JSONException, InvalidDataException {

        List<List<Element>> creationEventList = new ArrayList<List<Element>>();

        creationEventList = xmlToObject.parseXML(xml);
        elementDao.saveElements(creationEventList);

        return creationEventList.stream().flatMap(Collection::stream).map(e -> e.getId()).collect(Collectors.toList());

    }

}
