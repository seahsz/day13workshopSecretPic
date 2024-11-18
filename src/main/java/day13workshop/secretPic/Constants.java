package day13workshop.secretPic;

import java.util.Map;
import java.util.Random;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

public class Constants {


    public static final Map<String, String> USER_CREDENTIALS = Map.of(
        "johncena", "john10",
        "johnwick", "wick99",
        "donald", "donald200",
        "trump", "trump420",
        "elonmusk", "elon69",
        "kamala", "kamala77",
        "eastcoastplan", "ecp500"
    );

    public static final String ATTR_LOGIN_COUNT = "login_count";

    public static final String ATTR_USER = "userCredential";

    public static final String ATTR_CAPTCHA_ONE = "captchaNum1";

    public static final String ATTR_CAPTCHA_TWO = "captchaNum2";

    public static final String ATTR_CAPTCHA_RESULT = "captchaResult";


    // creates captcha, bind to model, store result in session
    public static void createCaptcha(Model model, HttpSession sess) {

        Random rand = new Random();

        int randOne = rand.nextInt(1, 100);
        int randTwo = rand.nextInt(1, 100);

        model.addAttribute(ATTR_CAPTCHA_ONE, randOne);
        model.addAttribute(ATTR_CAPTCHA_TWO, randTwo);

        // don't need to get, just set (as i don't care about the previous result)
        sess.setAttribute(ATTR_CAPTCHA_RESULT, randOne + randTwo);
        
    }
    
}
