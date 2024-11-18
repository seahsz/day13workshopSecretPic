package day13workshop.secretPic.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;

import day13workshop.secretPic.models.User;
import jakarta.servlet.http.HttpSession;

import static day13workshop.secretPic.Constants.*;

@Controller
@RequestMapping(path = "/login")
public class LoginController {

    @PostMapping
    public String postLogin(
            @Valid @ModelAttribute("userCredential") User userCred,
            BindingResult binding,
            @RequestParam(required = false) String captchaResult,
            Model model,
            HttpSession session) {

        /*
         * First, evaluate binding result, if not valid --> does not count as login
         * attempt
         * Need to do it before anything else because cannot bind new User() if got
         * binding result --> that would remove any validation when return to html
         */

        // get the login count from the session
        int currLoginCount = (Integer) session.getAttribute(ATTR_LOGIN_COUNT);

        if (binding.hasErrors()) {

            if (currLoginCount >= 3) {
                createCaptcha(model, session);
                return "captcha";
            }

            return "landingPage";
        }


        /*
         * evaluate Captcha second --> if fail, does NOT count as login attempt, hence
         * evaluate it second
         */

        if (captchaResult != null) {

            if (captchaResult.isEmpty()) {
                createCaptcha(model, session);

                binding.reject("global", "Please enter the captcha code");

                System.out.println(binding.toString());

                return "captcha";
            }

            int intCaptcha = Integer.parseInt(captchaResult);
            if (intCaptcha != (int) session.getAttribute(ATTR_CAPTCHA_RESULT)) {
                createCaptcha(model, session);
                return "captcha";
            }
        }

        model.addAttribute(ATTR_USER, new User());

        // increment loginCount by 1 (since reaching here would = +1 login attempt) and
        // set session login count to new count
        currLoginCount++;
        session.setAttribute(ATTR_LOGIN_COUNT, currLoginCount);

        System.out.println(">>>>>>> Login count: " + currLoginCount);

        // check whether login is successful
        // if contains username
        if (USER_CREDENTIALS.containsKey(userCred.getUsername())) {

            // check if username and password matches
            // if both matches
            if (USER_CREDENTIALS.get(userCred.getUsername()).equals(userCred.getPassword())) {

                // reset login attempt to 0 after successful login
                session.setAttribute(ATTR_LOGIN_COUNT, 0);
                return "success";

            }

            // do not match, check number of login attempts
            else {
                // >= 3 attempts need to do captcha
                if (currLoginCount >= 3) {

                    System.out.println("Entering captcha");
                    createCaptcha(model, session);

                    // // [try global error - to show login count]
                    // ObjectError err = new ObjectError("globalError",
                    //         "You have attempted to login %d times".formatted(currLoginCount));
                    // binding.addError(err);

                    // System.out.println("Does binding have errors: " + binding.hasErrors());
                    // System.out.println(binding.toString());

                    return "captcha";
                }

                // if < 3 attempts (1/2), send back to landing page
                return "landingPage";

            }
        }
        /*
         * if username is wrong, also check number of login attempts (to see if return
         * to captcha or landing page)
         */
        else {

            // >= 3 attempts need to do captcha
            if (currLoginCount >= 3) {
                createCaptcha(model, session);
                return "captcha";
            }

            // if < 3 attempts (1/2), send back to landing page
            return "landingPage";

        }

    }

    // to redirect people that attempt to access /login directly - instead of
    // producing 404
    @GetMapping
    public String getLogin(
            Model model,
            HttpSession session) {

        // bind new User() for form to bind data to field
        model.addAttribute(ATTR_USER, new User());

        // need to evaluate login count to determine if return to landing page or
        // captcha
        int currLoginCount = (int) session.getAttribute(ATTR_LOGIN_COUNT);

        if (currLoginCount >= 3) {
            createCaptcha(model, session);
            return "captcha";
        } else
            return "landingPage";

    }

}
