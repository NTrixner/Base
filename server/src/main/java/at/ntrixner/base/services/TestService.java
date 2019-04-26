package at.ntrixner.base.services;

import at.ntrixner.base.dto.MessageDTO;
import at.ntrixner.base.model.TestEntity;
import at.ntrixner.base.repositories.TestRepository;
import at.ntrixner.base.services.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class TestService {

    @Autowired
    private TestRepository repository;

    @Autowired
    private TestMapper mapper;


    public MessageDTO getMessage(Long id){
        return mapper.map(repository.findById(id).orElseThrow(InvalidParameterException::new));
    }

    public MessageDTO getMessage(String message){
        return mapper.map(message);
    }

    public TestEntity createMessage(String message){
        TestEntity entity = new TestEntity();
        entity.setMessage(message);
        return repository.save(entity);
    }

    public MessageDTO[] getAllmessages() {
        ArrayList<TestEntity> entities = new ArrayList<>();
        repository.findAll().forEach(testEntity -> entities.add(testEntity));
        return entities.stream().map(testEntity -> mapper.map(testEntity)).toArray(MessageDTO[]::new);
    }
}
