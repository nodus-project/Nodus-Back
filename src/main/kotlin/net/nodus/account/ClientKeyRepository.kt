package net.nodus.account

import org.springframework.data.mongodb.repository.MongoRepository

interface ClientKeyRepository : MongoRepository<ClientKey, String> {
    fun findByKey(key: String): ClientKey?
    fun existsByKey(key: String): Boolean
    fun findFirstByUserAccountId(userAccountId: String): ClientKey?
}
