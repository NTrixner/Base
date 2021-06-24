package eu.trixner.base.server.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
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

    @Override
    public String getErrorPath() {
        // Previously this had to return the error path we want to render,
        // but this is deprecated starting with 2.3.x Spring Boot
        return null;
    }
}