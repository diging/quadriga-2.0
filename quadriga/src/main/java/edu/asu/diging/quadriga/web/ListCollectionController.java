package edu.asu.diging.quadriga.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;
import edu.asu.diging.quadriga.core.service.CollectionManager;
import edu.asu.diging.quadriga.core.service.SimpleUserAppService;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

@Controller
public class ListCollectionController {

    @Autowired
    private CollectionManager collectionManager;
    
    @Autowired
    public SimpleUserAppService simpleUserAppService;

    @RequestMapping(value = "/auth/collections", method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model) {
    	
    	SimpleUser simpleUser = (SimpleUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	List<SimpleUserApp> userApps = simpleUserAppService.findByUsername(simpleUser.getUsername());
    	List<String> clientIds = null;
    	
    	if(userApps != null) {
    		clientIds = userApps.stream()
    				.map(userApp -> userApp.getAppClientId())
    				.collect(Collectors.toList());
    	}

        int page = 0;
        int size = 20;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        
        if(clientIds != null) {
            model.addAttribute("collections", collectionManager.findByAppsIn(clientIds, PageRequest.of(page, size)));
        }
        
        return "auth/showcollection";
    }

}
