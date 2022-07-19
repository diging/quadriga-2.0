package edu.asu.diging.quadriga.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CollectionRepository collectionRepo;

    @RequestMapping(value = "/auth/collections/archived", method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "size", required = false, defaultValue = "20") String size, Model model) {

        int pageInt;
        int sizeInt;

        try {
            pageInt = Integer.parseInt(page) - 1;
        } catch (NumberFormatException e) {
            logger.warn("Accessing invalid page", e);
            pageInt = 0;
        }
        
        try {
            sizeInt = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            logger.warn("Received invalid pageSize", e);
            sizeInt = 20;
        }

        model.addAttribute("collections", collectionRepo.findByIsArchived(true, PageRequest.of(pageInt, sizeInt)));
        return "auth/showArchivedCollections";
    }

}
