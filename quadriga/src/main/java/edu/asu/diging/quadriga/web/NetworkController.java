package edu.asu.diging.quadriga.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.quadriga.web.model.GraphEdgeData;
import edu.asu.diging.quadriga.web.model.GraphNodeData;

@Controller
public class NetworkController {
    
    @RequestMapping(value = "/auth/collections/{collectionId}/network/{networkId}")
    public String get(@PathVariable String collectionId, @PathVariable String networkId, Model model) {
        model.addAttribute("networkId", networkId);
        model.addAttribute("id1", "OMG!");
        
        GraphNodeData graphNodeData = new GraphNodeData();
        graphNodeData.setId("A");
        graphNodeData.setGroup(1);
        
        GraphNodeData graphNodeData2 = new GraphNodeData();
        graphNodeData2.setId("B");
        graphNodeData2.setGroup(0);
        
        GraphNodeData graphNodeData3 = new GraphNodeData();
        graphNodeData3.setId("C");
        graphNodeData3.setGroup(1);
        
        GraphEdgeData graphEdgeData1 = new GraphEdgeData();
        graphEdgeData1.setId("e1");
        graphEdgeData1.setSource(graphNodeData.getId());
        graphEdgeData1.setTarget(graphNodeData2.getId());
        
        GraphEdgeData graphEdgeData2 = new GraphEdgeData();
        graphEdgeData2.setId("e2");
        graphEdgeData2.setSource(graphNodeData3.getId());
        graphEdgeData2.setTarget(graphNodeData2.getId());
        
        
        
        return "auth/displayNetwork";
    }
    

}
;