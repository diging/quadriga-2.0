package edu.asu.diging.quadriga.converter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import edu.asu.diging.quadriga.domain.elements.Element;
import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;

public interface IXmltoObject {

    
    /**
     * The method parse XML for post request to add relation and appellation node into the database.
     * @param xml
     * @return
     * @throws ParserException
     * @throws IOException
     * @throws URISyntaxException
     * @throws ParseException
     * @throws InvalidDataException
     */
    List<List<Element>> parseXML(String xml) throws ParserException, IOException, URISyntaxException, ParseException, InvalidDataException;
    
    

}