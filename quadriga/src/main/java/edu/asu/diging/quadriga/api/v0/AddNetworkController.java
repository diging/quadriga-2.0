package edu.asu.diging.quadriga.api.v0;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.service.IRepositoryManager;

@Controller
public class AddNetworkController {

    private static final String JSON = "application/json";

    
    @Autowired
    private IRepositoryManager repositoryManager;
    
    /**
     * The method parse given Json from the post request body and add Network
     * instance to the database
     * 
     * @param request
     * @param response
     * @param xml
     * @param accept
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rest/add", method = RequestMethod.POST)
    public List<String> processJson(HttpServletRequest request, HttpServletResponse response, @RequestBody String json,
            @RequestHeader("Accept") String accept) { 

        if (json.equals("")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            List<String> errorResponse = new ArrayList<>();
            // errorResponse.add(new Message("Please provide XML in body of the post
            // request.").toString(accept));
            return errorResponse;
        }


        try {
            repositoryManager.processJsonAndStoreInDb(json);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(accept);
        return new ArrayList<>();

    }

}
