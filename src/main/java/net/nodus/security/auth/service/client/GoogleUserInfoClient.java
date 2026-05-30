package net.nodus.security.auth.service.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "googleUserInfoClient", url = "https://openidconnect.googleapis.com")
public interface GoogleUserInfoClient {

    @GetMapping("/v1/userinfo")
    Map<String, Object> fetchUserInfo(@RequestHeader("Authorization") String bearerToken);
}
