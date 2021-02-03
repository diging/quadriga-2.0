package edu.asu.diging.quadriga.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.asu.diging.quadriga.core.service.IAppManager;
import edu.asu.diging.quadriga.web.forms.AppForm;

@Controller
@PropertySource("classpath:locale/messages_en_US.properties")
public class AddAppController {

    @Autowired
    private Environment env;

    @Autowired
    private IAppManager appManager;

    @RequestMapping(value = "/admin/add", method = RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("app", new AppForm());
        return "admin/addApp";
    }

    @RequestMapping(value = "/admin/apps/add", method = RequestMethod.POST)
    public String add(@ModelAttribute AppForm appForm, RedirectAttributes redirectAttrs) {

        appManager.addApp(appForm);

        redirectAttrs.addFlashAttribute("alert_type", "success");
        redirectAttrs.addFlashAttribute("alert_msg", env.getRequiredProperty("app_create_success"));
        return "redirect:/admin/add";
    }

}
