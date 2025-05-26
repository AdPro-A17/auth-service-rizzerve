package rizzerve.authservice.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import rizzerve.authservice.model.Admin;
import rizzerve.authservice.security.token.TokenService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();

        Admin admin = new Admin();
        admin.setId(UUID.randomUUID());
        admin.setUsername("testuser");
        admin.setPassword("encodedPassword");
        userDetails = admin;
    }

    @Test
    void doFilterInternalShouldSkipIfNoAuthHeader() throws Exception {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, never()).extractUsername(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalShouldSkipIfHeaderDoesNotStartWithBearer() throws Exception {
        request.addHeader("Authorization", "Basic sometoken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(tokenService, never()).extractUsername(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
