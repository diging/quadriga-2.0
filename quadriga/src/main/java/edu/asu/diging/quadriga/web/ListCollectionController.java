package edu.asu.diging.quadriga.web;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

@Controller
public class ListCollectionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private CollectionManager collectionManager;
    
    @Autowired
    public SimpleUserAppService simpleUserAppService;
    
    @RequestMapping(value = "/auth/collections", method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "1", required = false, value = "page") String page,
            @RequestParam(defaultValue = "20", required = false, value = "size") String size, Model model, Authentication authentication) {

        SimpleUser simpleUser = (SimpleUser) authentication.getPrincipal();
        List<String> appClientIds = simpleUserAppService.findAppClientIdsByUsername(simpleUser.getUsername());
        
        Integer pageInt;
        Integer sizeInt;

        try {
            pageInt = new Integer(page) - 1;
        } catch (NumberFormatException ex) {
            logger.warn("Trying to access invalid page number: " + page, ex);
            pageInt = 0;
        }
        
        try {
            sizeInt = new Integer(size);
        } catch (NumberFormatException ex) {
            logger.warn("Received invalid page size: " + size, ex);
            sizeInt = 20;
        }

        if (appClientIds != null) {
            model.addAttribute("collections",collectionManager.findCollections(simpleUser.getUsername(), appClientIds,
                    PageRequest.of(pageInt, sizeInt))); 
            model.addAttribute("username", simpleUser.getUsername());
        }
        return "auth/showcollections";
    }

}
