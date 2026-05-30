package net.nodus.security.auth.service;

import net.nodus.database.account.OAuthProvider;
import net.nodus.database.account.UserAccount;
import net.nodus.database.account.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthLoginService {

    private final UserAccountRepository userAccountRepository;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenService refreshTokenService;

    public OAuthLoginService(
        UserAccountRepository userAccountRepository,
        JwtTokenService jwtTokenService,
        RefreshTokenService refreshTokenService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    public OAuthLoginResult loginGoogleUser(String providerId, String email, String name) {
        UserAccount user = userAccountRepository
            .findByProviderAndProviderIdAndDeletedAtIsNull(OAuthProvider.GOOGLE, providerId)
            .orElseGet(() -> userAccountRepository.save(
                new UserAccount(email, name, OAuthProvider.GOOGLE, providerId)
            ));

        RefreshTokenService.IssuedRefreshToken refreshToken = refreshTokenService.issue(user);

        return new OAuthLoginResult(
            jwtTokenService.createAccessToken(user),
            refreshToken.token()
        );
    }

    public record OAuthLoginResult(String accessToken, String refreshToken) {

    }
}
