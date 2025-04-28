package de.koerperschmiede.backend.models.dto.out;

public record UserDTO(
    String id,
    String firstName,
    String lastName,
    String birthdate,
    String email
) {
}

