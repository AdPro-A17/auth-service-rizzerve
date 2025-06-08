package rizzerve.authservice.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rizzerve.authservice.security.filter.JwtAuthFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityEndpointConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final SecurityCorsConfig corsConfig;
    private final SecurityAuthProviderConfig authConfig;

    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/**",
            "/api/customer/session/**",
            "/h2-console/**",
            "/api/table/**",
            "/api/orders/**",
            "/api/checkouts/**",
            "/actuator/**",
            "/api/metrics/**",
            "/api/test/**"
    );

    private static final List<String> ADMIN_ENDPOINTS = List.of("/api/admin/**");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(
                        authConfig.getSessionCreationPolicy()))
                .authenticationProvider(authConfig.authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(this::configureAuthorization)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .build();
    }

    private void configureAuthorization(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
        auth.requestMatchers(PUBLIC_ENDPOINTS.toArray(new String[0])).permitAll()
                .requestMatchers(ADMIN_ENDPOINTS.toArray(new String[0])).hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
    }
}