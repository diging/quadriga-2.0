package edu.asu.diging.quadriga.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResetPasswordRequestSentController {

    @RequestMapping("/reset/request/sent")
    public String show() {
        return "resetRequestSent";
    }
}