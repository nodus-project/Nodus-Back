package net.nodus.security

import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.nodus.account.UserAccountRepository
import net.nodus.auth.JwtTokenService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val userAccountRepository: UserAccountRepository
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath

        return path.startsWith("/oauth2")
                || path.startsWith("/login/oauth2")

    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if(SecurityContextHolder.getContext().authentication != null) {
            filterChain.doFilter(request, response)
            return
        }

        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION)

        if(authorization != null && authorization.startsWith("Bearer ")) {
            val token = authorization.removePrefix("Bearer ").trim()

            try {
                val claims = jwtTokenService.parseClaims(token)
                val userId = UUID.fromString(claims.subject)
                val user = userAccountRepository.findByIdOrNull(userId)

                if(user != null && user.id != null) {
                    val principal = AuthUserPrincipal(
                        id = user.id.toString(),
                        email = user.email
                    )

                    val authentication = UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        listOf(SimpleGrantedAuthority("ROLE_USER"))
                    )

                    authentication.details =
                        WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (_: JwtException) {
                SecurityContextHolder.clearContext()
            } catch (_: IllegalArgumentException) {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}