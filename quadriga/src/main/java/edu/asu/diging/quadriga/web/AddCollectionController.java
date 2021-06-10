package edu.asu.diging.quadriga.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.mongo.CollectionRepository;
import edu.asu.diging.quadriga.domain.elements.Collection;
import edu.asu.diging.quadriga.service.ICollectionManager;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {

    @Autowired
    private ICollectionManager collectionManager;
    
    @Autowired
    private CollectionRepository collectionRepo;

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        return "admin/user/addCollection";
    }
    
	
    @RequestMapping(value="/auth/collections",method=RequestMethod.GET) 
    public String showCollection(Model model) {
    	model.addAttribute("collections",collectionRepo.findAll()); 
    	return "admin/user/showcollection"; 
    }
	 

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.POST)
    public String add(@Valid CollectionForm collectionForm, BindingResult result,
            RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "admin/user/addCollection";
        }
        Collection collection=new Collection();
        collection.setName(collectionForm.getName());
        collection.setDescription(collection.getDescription());
        collectionManager.addCollection(collection);
        
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Collection has been added.");
        return "redirect:/auth/collections";
    }

}
