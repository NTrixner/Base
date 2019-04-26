package at.ntrixner.base.services.mapper;

import at.ntrixner.base.dto.MessageDTO;
import at.ntrixner.base.model.TestEntity;
import org.springframework.stereotype.Component;

@Component
public class TestMapper {
    public MessageDTO map(TestEntity entity){
        MessageDTO dto = new MessageDTO();
        dto.setMessage(entity.getMessage());
        return dto;
    }

    public MessageDTO map(String message){
        MessageDTO dto = new MessageDTO();
        dto.setMessage(message);
        return dto;
    }
}
