package edu.asu.diging.quadriga.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.exceptions.UnstorableObjectException;
import edu.asu.diging.quadriga.core.service.IAppManager;
import edu.asu.diging.quadriga.web.forms.AppForm;


@Controller
public class AddAppController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IAppManager appManager;


    @RequestMapping(value = "/admin/add", method=RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("app", new AppForm());
        return "addApp";
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    public String add(@ModelAttribute AppForm appForm, RedirectAttributes redirectAttrs) {
        
        try {
            appManager.addApp(appForm);
        } catch (UnstorableObjectException e) {
            logger.error("Could not store Group", e);
            redirectAttrs.addFlashAttribute("show_alert", true);
            redirectAttrs.addFlashAttribute("alert_type", "danger");
            redirectAttrs.addFlashAttribute("alert_msg", "App creation failed. New group couldn't be stored.");
            return "redirect:/";
        }

        redirectAttrs.addFlashAttribute("show_alert", true);
        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", "App was successfully added.");
        return "redirect:/";
    }

}
