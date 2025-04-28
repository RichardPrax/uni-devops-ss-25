package de.koerperschmiede.backend.models.dto.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewUserDTO(
    @NotBlank(message = "First name must not be blank")
    String firstName,

    @NotBlank(message = "Last name must not be blank")
    String lastName,

    @NotNull(message = "Birth date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate birthdate,

    @Nullable
    String password,

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    String email
) {
}


