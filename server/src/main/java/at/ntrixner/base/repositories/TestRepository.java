package at.ntrixner.base.repositories;

import at.ntrixner.base.model.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<TestEntity, Long> {
}
