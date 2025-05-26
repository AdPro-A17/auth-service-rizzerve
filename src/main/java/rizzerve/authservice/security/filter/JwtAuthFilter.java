package rizzerve.authservice.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rizzerve.authservice.security.token.TokenService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final AdminTokenService adminTokenService;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (!isValidAuthHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = extractJwtFromHeader(authHeader);

        if (adminTokenService.isTokenBlacklisted(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String username = tokenService.extractUsername(jwt);

        if (username != null && isAuthenticationRequired()) {
            processAuthentication(request, jwt, username);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
    }

    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(BEARER_PREFIX.length());
    }

    private boolean isAuthenticationRequired() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void processAuthentication(HttpServletRequest request, String jwt, String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (tokenService.validateToken(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = createAuthenticationToken(userDetails);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}