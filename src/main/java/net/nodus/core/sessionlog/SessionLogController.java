package net.nodus.core.sessionlog;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.nodus.core.sessionlog.dto.CreateSessionLogRequest;
import net.nodus.core.sessionlog.dto.SessionLogResponse;
import net.nodus.database.sessionlog.SessionLog;
import net.nodus.global.common.response.ApiPayload;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "세션 로그", description = "세션 로그 API")
@RestController
@RequestMapping("/session-logs")
public class SessionLogController {

    private final SessionLogService sessionLogService;

    public SessionLogController(SessionLogService sessionLogService) {
        this.sessionLogService = sessionLogService;
    }

    @Operation(
        summary = "세션 로그 생성",
        description = "사이트 키를 인증하고 세션 로그를 저장합니다."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiPayload<SessionLogResponse> create(
        @Valid @RequestBody CreateSessionLogRequest request) {
        SessionLog sessionLog = sessionLogService.create(request.getSiteKey(),
            request.getUserSession());

        return ApiPayload.success(SessionLogResponse.from(sessionLog));
    }
}
