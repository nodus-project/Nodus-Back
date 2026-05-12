package net.nodus.auth.service.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "googleTokenClient", url = "https://oauth2.googleapis.com")
interface GoogleTokenClient {
    @PostMapping(value = ["/token"], consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun exchangeCode(params: Map<String, *>): Map<String, Any>
}

@FeignClient(name = "googleUserInfoClient", url = "https://openidconnect.googleapis.com")
interface GoogleUserInfoClient {
    @GetMapping("/v1/userinfo")
    fun fetchUserInfo(@RequestHeader("Authorization") bearerToken: String): Map<String, Any>
}