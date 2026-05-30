package net.nodus.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.nodus.database.account.OAuthProvider;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import net.nodus.global.common.exception.GlobalException;
import net.nodus.security.auth.dto.OAuthResponse;
import net.nodus.security.auth.service.JwtTokenService;
import net.nodus.security.auth.service.RefreshTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserAccountRepository userAccountRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        UserAccount user = findOrCreateUser(oidcUser);
        String accessToken = jwtTokenService.createAccessToken(user);
        RefreshTokenService.IssuedRefreshToken refreshToken = refreshTokenService.issue(user);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OAuthResponse.OAuthLoginResponse oAuthLoginResponse = OAuthResponse.OAuthLoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken.token())
            .build();

        objectMapper.writeValue(
            response.getWriter(),
            oAuthLoginResponse
        );
    }

    private UserAccount findOrCreateUser(OidcUser oidcUser) {
        String providerId = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        if (email == null) {
            throw new GlobalException.ExternalApiFailed("Google account email is missing");
        }
        String name = oidcUser.getFullName() == null ? email : oidcUser.getFullName();

        return userAccountRepository
            .findByProviderAndProviderIdAndDeletedAtIsNull(OAuthProvider.GOOGLE, providerId)
            .map(existingUser -> {
                existingUser.setEmail(email);
                existingUser.setName(name);
                return userAccountRepository.save(existingUser);
            })
            .orElseGet(() -> userAccountRepository.save(
                new UserAccount(email, name, OAuthProvider.GOOGLE, providerId)
            ));
    }
}
