package net.nodus.sessionlog.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionLogRepository : MongoRepository<SessionLog, String>