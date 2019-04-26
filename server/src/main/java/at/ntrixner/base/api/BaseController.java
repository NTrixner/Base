package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@RestController
@RequestMapping("/base")
public class BaseController implements BaseAPI {

    public static final Logger DEBUG = Logger.getLogger(BaseController.class.getName());

    @Value("${server.application.name}")
    String applicationName;

    @PostConstruct
    public void PostConstruct(){
        DEBUG.info(applicationName);
    }

    @Override
    @GetMapping("/test")
    public MessageDTO getTestMessage() {
        MessageDTO dto = new MessageDTO();
        dto.setMessage("Hello World!");
        return dto;
    }

    @Override
    @GetMapping("/appName")
    public MessageDTO getAppName(){
        MessageDTO dto = new MessageDTO();
        dto.setMessage(applicationName);
        return dto;
    }
}
