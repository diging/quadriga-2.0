package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.mongo.CollectionRepository;

@Controller
public class ShowCollectionController {

	@Autowired
	private CollectionRepository collectionRepo;

	@RequestMapping(value="/auth/collections",method=RequestMethod.GET) 
	public String showCollection(Model model) {
		model.addAttribute("collections",collectionRepo.findAll()); 
		return "admin/user/showcollection"; 
	}

}
