package edu.asu.diging.quadriga.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.service.ICollectionManager;
import edu.asu.diging.quadriga.service.impl.CollectionValidator;
import edu.asu.diging.quadriga.web.forms.CollectionForm;

@Controller
public class AddCollectionController {

    @Autowired
    private ICollectionManager collectionManager;

    @Autowired
    private CollectionValidator collectionValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(collectionValidator);
    }

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("collection", new CollectionForm());
        return "admin/user/addCollection";
    }

    @RequestMapping(value = "/auth/collections/add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Validated CollectionForm collectionForm, BindingResult result,
            RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "admin/user/addCollection";
        }

        collectionManager.addCollection(collectionForm);

        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "Collection has been added.");
        return "redirect:/auth/collections/add";
    }

}
