package net.nodus.account

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ClientKeyRepository : JpaRepository<ClientKey, Long> {
    fun findByKey(key: String): ClientKey?
    fun existsByKey(key: String): Boolean

    @Query("SELECT c FROM ClientKey c where c.userAccount.id = :userAccountId")
    fun findFirstByUserAccountId(@Param("userAccountId") userAccountId: UUID): ClientKey?
}