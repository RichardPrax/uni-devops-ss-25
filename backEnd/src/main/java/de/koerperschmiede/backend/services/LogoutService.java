package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.config.JwtService;
import de.koerperschmiede.backend.models.entities.TokenEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TokenRepository;
import de.koerperschmiede.backend.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        TokenEntity storedToken = tokenRepository.findByToken(jwt)
            .orElse(null);

        String username = jwtService.extractUsername(jwt);
        Optional<UserEntity> user = userRepository.findByEmail(username);
        user.ifPresent(userRepository::save);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
