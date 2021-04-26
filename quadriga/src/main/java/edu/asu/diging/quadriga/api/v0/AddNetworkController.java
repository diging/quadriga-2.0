package edu.asu.diging.quadriga.api.v0;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddNetworkController {

    private static final String XML = "application/xml";
    private static final String JSON = "application/json";


    /**
     * The method parse given XML from the post request body and add relation and
     * appellation event into the database
     * 
     * @param request
     * @param response
     * @param xml
     * @param accept
     * @return
     * @throws ParseException
     * @throws IOException
     * @throws ParserException
     * @throws JSONException
     * @throws URISyntaxException
     * @throws InvalidDataException
     */
    @ResponseBody
    @RequestMapping(value = "/rest/add", method = RequestMethod.POST)
    public List<String> processXML(HttpServletRequest request, HttpServletResponse response, @RequestBody String xml,
            @RequestHeader("Accept") String accept){ //throws ParserException, IOException, URISyntaxException,
            //ParseException, JSONException, InvalidDataException {

        if (xml.equals("")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            List<String> errorResponse = new ArrayList<>();
            //errorResponse.add(new Message("Please provide XML in body of the post request.").toString(accept));
            return errorResponse;
        }

        List<String> returnIds = new ArrayList<>();

        if (accept != null && accept.equals(JSON)) {
           // returnIds = repositorymanager.processXMLandStoretoDb(xml, JSON);
        } else {
          //  returnIds = repositorymanager.processXMLandStoretoDb(xml, XML);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(accept);
        return returnIds;

    }

}
