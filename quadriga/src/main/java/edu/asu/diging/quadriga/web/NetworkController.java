package edu.asu.diging.quadriga.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NetworkController {
    
    @RequestMapping(value = "/auth/collections/{collectionId}/network/{networkId}")
    public String get(@PathVariable String collectionId, @PathVariable String networkId, Model model) {
        model.addAttribute("networkId", networkId);
        return "auth/displayNetwork";
    }
    

}
