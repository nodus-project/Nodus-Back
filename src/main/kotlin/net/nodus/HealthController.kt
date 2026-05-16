package net.nodus

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Health", description = "서버 상태 확인 API")
@RestController
class HealthController {
    @Operation(summary = "헬스 체크", description = "서버가 정상적으로 응답 가능한 상태인지 확인합니다.")
    @GetMapping("/")
    fun index(): Map<String, String> {
        return mapOf(
            "status" to "ok",
            "application" to "nodus"
        )
    }
}
