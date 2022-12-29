package eu.trixner.base.server.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController implements ErrorController {

    public static final String MAIN_PAGE_URL = "/";

    @GetMapping(MAIN_PAGE_URL)
    public String mainPage() {
        return "index.html";
    }


    @GetMapping("/error")
    public String error() {
        return "index.html";
    }
}
