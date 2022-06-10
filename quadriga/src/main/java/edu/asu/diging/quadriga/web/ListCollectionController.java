package edu.asu.diging.quadriga.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.quadriga.core.data.CollectionRepository;

@Controller
public class ListCollectionController {

    @Autowired
    private CollectionRepository collectionRepo;

    @RequestMapping(value = "/auth/collections", method = RequestMethod.GET)
    public String list(HttpServletRequest request, Model model) {

        int page = 0;
        int size = 20;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }

        model.addAttribute("collections", collectionRepo.findAll(PageRequest.of(page, size)));
        return "auth/showcollections";
    }

}
