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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityCorsConfig corsConfig,
                                                   SecurityAuthProviderConfig authConfig) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(
                        authConfig.getSessionCreationPolicy()))
                .authenticationProvider(authConfig.authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        configureAuthorization(http);

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    private void configureAuthorization(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(getPublicEndpoints().toArray(new String[0])).permitAll()
                .requestMatchers(getAdminEndpoints().toArray(new String[0])).hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
        );
    }

    protected List<String> getPublicEndpoints() {
        return List.of(
                "/api/auth/**",
                "/api/customer/session/**",
                "/h2-console/**",
                "/api/table/**"
        );
    }

    protected List<String> getAdminEndpoints() {
        return List.of("/api/admin/**");
    }
}