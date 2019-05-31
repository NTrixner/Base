package at.ntrixner.base.api;

import at.ntrixner.base.dto.MessageDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class InternalController implements InternalAPI {

    @Override
    @GetMapping("/greeting")
    public MessageDTO getInternalMessage() {
        MessageDTO dto = new MessageDTO();
        dto.setMessage("You are logged in!");
        return dto;
    }
}
