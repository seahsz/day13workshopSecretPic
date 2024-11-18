package day13workshop.secretPic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import day13workshop.secretPic.models.User;
import jakarta.servlet.http.HttpSession;

import static day13workshop.secretPic.Constants.*;

@Controller
@RequestMapping(path = { "/", "index.html" })
public class LandingPageController {

    @GetMapping
    public String getLandingPage(
            HttpSession session,
            Model model) {

        // so that the form can bind the values to the members --> so that the fields can be validated
        model.addAttribute(ATTR_USER, new User());

        // NOTE: Integer can be "null", int cannot be "null"
        Integer loginCount = (Integer)session.getAttribute(ATTR_LOGIN_COUNT);

        if (loginCount == null) {
            loginCount = 0;
            session.setAttribute(ATTR_LOGIN_COUNT, loginCount);
        }
        
        return "landingPage";
    }

    @GetMapping("/logout")
    public String getLogout(HttpSession sess) {

        sess.invalidate();
        return "redirect:/";
    }

}
