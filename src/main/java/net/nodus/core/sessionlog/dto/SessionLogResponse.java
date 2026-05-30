package net.nodus.core.sessionlog.dto;

import net.nodus.database.sessionlog.SessionLog;

import java.util.Objects;

public record SessionLogResponse(
        String id,
        String siteKeyId,
        String userAccountId,
        String siteId,
        String userSession,
        String createdAt
) {
    public static SessionLogResponse from(SessionLog sessionLog) {
        return new SessionLogResponse(
                Objects.requireNonNull(sessionLog.getId()),
                sessionLog.getSiteKeyId(),
                sessionLog.getUserAccountId(),
                sessionLog.getSiteId(),
                sessionLog.getUserSession(),
                sessionLog.getCreatedAt().toString()
        );
    }
}
