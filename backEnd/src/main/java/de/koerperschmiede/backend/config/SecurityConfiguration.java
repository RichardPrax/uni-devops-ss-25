package de.koerperschmiede.backend.config;

import de.koerperschmiede.backend.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**", "/actuator/health"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    // TODO: should we define authorization for the endpoints in the controller classes or general here
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3000"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))
            .authorizeHttpRequests(req -> req
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers("/api/v1/general-exercises/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.POST, "/api/v1/training-plans/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.PUT, "/api/v1/training-plans/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/training-plans/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.POST, "/api/v1/custom-exercises/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.PUT, "/api/v1/custom-exercises/**").hasRole(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.DELETE, "/api/v1/custom-exercises/**").hasRole(Role.ADMIN.toString())
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response,
                                       authentication) -> SecurityContextHolder
                    .clearContext()));

        return http.build();
    }
}
