package net.nodus.security.auth.service.client;

import java.util.Map;
import net.nodus.security.auth.service.GoogleOAuthService.GoogleTokenInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "googleTokenClient", url = "https://oauth2.googleapis.com")
public interface GoogleTokenClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenInfo exchangeCode(Map<String, String> params);

}
