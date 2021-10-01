package edu.asu.diging.quadriga.api.v1;

import java.util.List;
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
import edu.asu.diging.quadriga.api.v1.model.TokenInfo;
import edu.asu.diging.quadriga.core.exception.NodeNotFoundException;
import edu.asu.diging.quadriga.core.exceptions.OAuthException;
import edu.asu.diging.quadriga.core.model.EventGraph;
import edu.asu.diging.quadriga.core.model.events.CreationEvent;
import edu.asu.diging.quadriga.core.service.EventGraphService;
import edu.asu.diging.quadriga.core.service.MappedTripleService;
import edu.asu.diging.quadriga.core.service.NetworkMapper;
import edu.asu.diging.quadriga.core.service.TokenValidator;

@Controller
public class AddNetworkApiController {

    @Autowired
    private NetworkMapper networkMapper;

    @Autowired
    private EventGraphService eventGraphService;

    @Autowired
    private MappedTripleService mappedTripleService;
    
    @Autowired
    private TokenValidator tokenValidator;

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
    public HttpStatus processJson(@RequestBody Quadruple quadruple, @RequestHeader(name = "Authorization",  required = true) String authHeader) {

        if (quadruple == null) {
            return HttpStatus.NO_CONTENT;
        }
        
        String token = getTokenFromHeader(authHeader);
        if(token == null) {
            return HttpStatus.NOT_FOUND;
        }
        
        TokenInfo tokenInfo;
        try {
            tokenInfo = tokenValidator.getTokenInfo(token);
            
            // either token info wasn't returned by citesphere or the token has expired
            if(tokenInfo == null || !tokenInfo.isActive()) {
                return HttpStatus.UNAUTHORIZED;
            }
            
        } catch (OAuthException e) {
            
            // we got unauth twice (using existing access token and re-generated one)
            return HttpStatus.UNAUTHORIZED;
        } catch(BadCredentialsException e) {
            
            //Token is invalid
            return HttpStatus.FORBIDDEN;
        }
        
        // the flow will reach  here  only when token is present, valid  and active

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
    
    
    /**
     * This method will use {@link edu.asu.diging.quadriga.core.service.impl.TokenValidatorImpl} to
     * check the validity of the token received from Vogon
     * 
     * @param authHeader is where the token would be present as 'Bearer {token}'
     * @return the HTTP Status as per the response by the validateToken method
     */
    private String getTokenFromHeader(String authHeader) {
        String token = null;
        
        if(authHeader.trim().isEmpty()) {
            return token;
        } else {
            // Trims the string "Bearer " to extract the exact token from the Authorization Header
            token = authHeader.substring(7);
            if(token.trim().isEmpty()) {
                return null;
            }
        }
        return token;
    }

}