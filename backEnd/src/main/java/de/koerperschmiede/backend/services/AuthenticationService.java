package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.config.JwtService;
import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.models.dto.in.AuthenticationRequest;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.dto.out.AuthenticationResponse;
import de.koerperschmiede.backend.models.entities.TokenEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TokenRepository;
import de.koerperschmiede.backend.repositories.UserRepository;
import de.koerperschmiede.backend.util.Role;
import de.koerperschmiede.backend.util.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationResponse register(NewUserDTO newUserDTO) {
        // create Address
        UserEntity user = UserEntity.builder()
            .firstName(newUserDTO.firstName())
            .lastName(newUserDTO.lastName())
            .email(newUserDTO.email())
            .password(passwordEncoder.encode(newUserDTO.password()))
            .role(Role.USER)
            .birthdate(newUserDTO.birthdate())
            .build();

        UserEntity savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        return new AuthenticationResponse(
            savedUser.getId().toString(),
            jwtToken,
            refreshToken,
            String.valueOf(savedUser.getRole())
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()));

        var user = userRepository.findByEmail(request.email());

        if (user.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        String jwtToken = jwtService.generateToken(user.get());
        String refreshToken = jwtService.generateRefreshToken(user.get());
        revokeAllUserTokens(user.get());
        saveUserToken(user.get(), jwtToken);
        userRepository.save(user.get());

        return new AuthenticationResponse(
            user.get().getId().toString(),
            jwtToken,
            refreshToken,
            String.valueOf(user.get().getRole())
        );

    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        List<TokenEntity> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    // TODO: brauchen wir den admin check überhaupt noch wenn wir das umbauen mit einer pre authorize kontrolle?
    public void verifyUserAccess(UUID userID) {
        UserEntity principal = getUserEntity();

        if (isAdmin(principal)) return; // admin can access everything

        if (!principal.getId().equals(userID)) {
            throw new AccessException("You do not have permission to access this resource");
        }
    }

    public UserEntity getUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // TODO: do we need this check? endpoints can only be accessed with authentication
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AccessException("User is not authenticated");
        }
        return (UserEntity) authentication.getPrincipal();
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRole() == Role.ADMIN;
    }
}
