package de.koerperschmiede.backend.models.dto.out;

public record AuthenticationResponse(
    String userId,
    String accessToken,
    String refreshToken,
    String role
) {
}
