package app.batch.repository;

import app.batch.domain.CustomerLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogRepository extends MongoRepository<CustomerLog, Integer> {
}
