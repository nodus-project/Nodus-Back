package net.nodus.database.sessionlog;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionLogRepository extends MongoRepository<SessionLog, String> {

}
