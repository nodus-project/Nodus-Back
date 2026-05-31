package net.nodus.security.auth.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import net.nodus.global.common.exception.UserAuthException;
import net.nodus.security.auth.controller.dto.UserAccountDetails;
import net.nodus.security.auth.service.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthManager {

    private final JwtProvider jwtProvider;
    private final String REFRESH_TOKEN = "refreshToken";

    public void updateAuthData(UserAccountDetails userAccountDetails,
        HttpServletResponse response) {
        String accessToken = jwtProvider.createAccessToken(userAccountDetails);
        String refreshToken = jwtProvider.createRefreshToken(userAccountDetails);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, createRefreshCookie(refreshToken).toString());
    }

    public UserAccountDetails parseAccessToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws UserAuthException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UserAuthException();
        }

        String accessToken = authorization.substring(7).trim();
        if (accessToken.isEmpty()) {
            throw new UserAuthException();
        }

        try {
            return jwtProvider.parseAccessToken(accessToken);
        } catch (ExpiredJwtException ex) {

            String refresh = getRefreshCookieValue(request);
            if (refresh == null || refresh.isBlank()) {
                throw new UserAuthException();
            }

            try {
                UserAccountDetails userAccountDetails = jwtProvider.parseRefreshToken(refresh);
                String newAccessToken = jwtProvider.createAccessToken(userAccountDetails);
                String refreshToken = jwtProvider.createRefreshToken(userAccountDetails);

                response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);
                response.addHeader(HttpHeaders.SET_COOKIE,
                    createRefreshCookie(refreshToken).toString());
                return userAccountDetails;
            } catch (JwtException | IllegalArgumentException refreshEx) {
                throw new UserAuthException();
            }

        } catch (JwtException | IllegalArgumentException ex) {
            throw new UserAuthException();
        }
    }


    private ResponseCookie createRefreshCookie(String refreshToken) {
        long refreshTokenExpirationSeconds = 60L * 60 * 24 * 7 * 4;

        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofSeconds(refreshTokenExpirationSeconds))
            .build();
    }

    private String getRefreshCookieValue(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(REFRESH_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
