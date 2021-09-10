package edu.asu.diging.quadriga.api.v1;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.diging.quadriga.api.v1.model.Quadruple;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.NetworkMapper;
import edu.asu.diging.quadriga.core.service.impl.TokenValidatorImpl;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;

@Controller
public class AddNetworkApiController {

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleService mappedTripleService;
    
    @Autowired
    private TokenValidatorImpl tokenValidatorImpl;

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
    @RequestMapping(value = "/api/v1/network/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @RequestHeader(name = "Authorization") String authHeader) {

        if (quadruple == null) {
            return HttpStatus.NO_CONTENT;
        }
        
        HttpStatus httpStatus = checkTokenValidity(authHeader);
        if(!HttpStatus.ACCEPTED.equals(httpStatus)) {
            return httpStatus;
        }
        

        // save network
        List<CreationEvent> events = networkMapper.mapNetworkToEvents(quadruple.getGraph());
        List<EventGraph> eventGraphs = events.stream().map(e -> new EventGraph(e)).collect(Collectors.toList());
        eventGraphs.forEach(e -> e.setDefaultMapping(quadruple.getGraph().getMetadata().getDefaultMapping()));
        eventGraphService.saveEventGraphs(eventGraphs);

        try {
            mappedTripleService.storeMappedGraph(quadruple.getGraph());
        } catch (NodeNotFoundException e1) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.ACCEPTED;

    }
    
    private HttpStatus checkTokenValidity(String authHeader) {
        String token = null;
        
        if(Objects.isNull(authHeader) || authHeader.isBlank()) {
            return HttpStatus.BAD_REQUEST;
        } else {
            // Trims the string "Bearer " to extract the exact token from the Authorization Header
            token = authHeader.substring(7);
            if(Objects.isNull(token) || token.isBlank()) {
                return HttpStatus.BAD_REQUEST;
            }
        }
        
        try {
            if(!tokenValidatorImpl.validateToken(token)) {
                // token has expired
                return HttpStatus.UNAUTHORIZED;
            }
        } catch (TokenExpiredException | OAuthException e) {
            // token has either expired or citesphere sent an empty response
            return HttpStatus.UNAUTHORIZED;
        } catch (BadCredentialsException bce) {
            // the server understood the request but refuses to authorize it.
            return HttpStatus.FORBIDDEN;
        }
        
        // token is active
        return HttpStatus.ACCEPTED;
    }

}