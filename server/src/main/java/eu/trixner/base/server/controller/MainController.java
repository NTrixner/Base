package eu.trixner.base.server.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Slf4j
public class MainController implements ErrorController {

    public static final String MAIN_PAGE_URL = "/";

    @GetMapping(MAIN_PAGE_URL)
    public String mainPage() {
        log.info("Redirecting to index.html");
        return "index.html";
    }


    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public String error() {
        log.info("Forwarding to index.html");
        return "forward:/index.html";
    }
}
