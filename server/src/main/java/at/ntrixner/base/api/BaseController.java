package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import at.ntrixner.base.model.TestEntity;
import at.ntrixner.base.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@RestController
@RequestMapping("/base")
public class BaseController implements BaseAPI {

    public static final Logger DEBUG = Logger.getLogger(BaseController.class.getName());

    @Value("${server.application.name}")
    String applicationName;

    @Autowired
    TestService testService;

    Long latestId;

    @PostConstruct
    public void PostConstruct(){
        DEBUG.info(applicationName);
        TestEntity entity = testService.createMessage("This is a database message");
        latestId = entity.getId();
        DEBUG.info("Created test message with id " + entity.getId());
    }

    @Override
    @GetMapping("/test")
    public MessageDTO getTestMessage() {
        return testService.getMessage("Hello World!");
    }

    @Override
    @GetMapping("/appName")
    public MessageDTO getAppName(){
        return testService.getMessage(applicationName);
    }

    @Override
    @GetMapping("/message/{id}")
    public MessageDTO getMessage(@PathVariable("id") Long id){
        return testService.getMessage(id);
    }

    @Override
    @GetMapping("/db")
    public MessageDTO getDB() {
        return testService.getMessage(latestId);
    }

    @Override
    @GetMapping("/messages")
    public MessageDTO[] getAll() {
        return testService.getAllmessages();
    }
}
