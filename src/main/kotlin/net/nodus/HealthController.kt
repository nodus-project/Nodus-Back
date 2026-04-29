package net.nodus

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/")
    fun index(): Map<String, String> {
        return mapOf(
            "status" to "ok",
            "application" to "nodus"
        )
    }
}
