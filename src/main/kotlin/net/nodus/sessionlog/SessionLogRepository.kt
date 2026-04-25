package net.nodus.sessionlog

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionLogRepository : JpaRepository<SessionLog, Long> {
}