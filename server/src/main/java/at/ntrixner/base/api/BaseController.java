package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Message;

@RestController
@RequestMapping("/base")
public class BaseController implements BaseAPI {

    @Override
    @GetMapping("/test")
    public MessageDTO getTestMessage() {
        MessageDTO dto = new MessageDTO();
        dto.setMessage("Hello World!");
        return dto;
    }
}
