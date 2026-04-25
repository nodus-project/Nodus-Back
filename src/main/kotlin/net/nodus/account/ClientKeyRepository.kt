package net.nodus.account

import org.springframework.data.jpa.repository.JpaRepository

interface ClientKeyRepository : JpaRepository<ClientKey, Long> {
    fun findByKey(key: String): ClientKey?
}