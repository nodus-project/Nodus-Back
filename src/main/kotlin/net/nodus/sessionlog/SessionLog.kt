package net.nodus.sessionlog

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.nodus.account.ClientKey
import java.time.Instant

@Entity
@Table(name = "session_logs")
class SessionLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_key_id")
    val clientKey: ClientKey,

    @Column(name = "user_session", nullable = false)
    val userSession: String,

    var createdAt: Instant = Instant.now(),
)