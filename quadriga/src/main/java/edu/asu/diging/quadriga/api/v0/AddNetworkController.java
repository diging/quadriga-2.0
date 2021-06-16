package edu.asu.diging.quadriga.api.v0;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.asu.diging.quadriga.service.IRepositoryManager;

@Controller
public class AddNetworkController {

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
    @RequestMapping(value = "/api/v1/network/add", method = RequestMethod.POST)
    public HttpStatus processJson(@RequestBody String json) {

    	json.trim();
        if (json.equals("")) {
            return HttpStatus.NO_CONTENT;
        }

        try {

            repositoryManager.processJsonAndStoreInDb(json);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.ACCEPTED;

    }

}
