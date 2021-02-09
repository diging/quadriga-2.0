package edu.asu.diging.quadriga.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.exceptions.InvalidDataException;
import edu.asu.diging.quadriga.exceptions.ParserException;

@Service
public interface IRepositoryManager {

    /**
     * The method call parser to parse xml; call storagemanager to store
     * relation and appellation event into the database and call converter to
     * convert list of the objects into the database.
     * 
     * @param xml
     * @param type
     * @return
     * @throws URISyntaxException
     * @throws ParserException
     * @throws IOException
     * @throws ParseException
     * @throws JSONException
     * @throws InvalidDataException
     */
    public List<String> processXMLandStoretoDb(String xml, String type) throws URISyntaxException, ParserException,
            IOException, ParseException, JSONException, InvalidDataException;
}
