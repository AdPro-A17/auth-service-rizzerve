package rizzerve.authservice.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import rizzerve.authservice.security.token.TokenService;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalShouldContinueWhenNoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenService, userDetailsService);
    }

    @Test
    void doFilterInternalShouldContinueWhenNonBearerAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenService, userDetailsService);
    }

    @Test
    void doFilterInternalShouldContinueWhenNoUsernameExtracted() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(tokenService.extractUsername("token123")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenService).extractUsername("token123");
        verifyNoMoreInteractions(tokenService);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternalShouldAuthenticateValidToken() throws ServletException, IOException {
        String username = "user@example.com";
        String token = "token123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(tokenService.validateToken(token, userDetails)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(tokenService).validateToken(token, userDetails);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalShouldNotAuthenticateInvalidToken() throws ServletException, IOException {
        String username = "user@example.com";
        String token = "token123";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(tokenService.validateToken(token, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(tokenService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(tokenService).validateToken(token, userDetails);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}