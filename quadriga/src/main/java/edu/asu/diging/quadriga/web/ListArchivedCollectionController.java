package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.asu.diging.quadriga.core.data.CollectionRepository;

@Controller
public class ListArchivedCollectionController {

    @Autowired
    private CollectionRepository collectionRepo;

    @RequestMapping(value = "/auth/archived-collections", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "size", required = false, defaultValue = "20") String size, Model model) {

        int pageInt = 0;
        int sizeInt = 20;

        pageInt = Integer.parseInt(page) - 1;
        sizeInt = Integer.parseInt(size);

        model.addAttribute("collections", collectionRepo.findByIsArchived(true, PageRequest.of(pageInt, sizeInt)));
        return "auth/showArchivedCollections";
    }

}
