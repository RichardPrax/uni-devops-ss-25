package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.exceptions.AlreadyExistsException;
import de.koerperschmiede.backend.models.dto.in.AuthenticationRequest;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.dto.out.AuthenticationResponse;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.services.AuthenticationService;
import de.koerperschmiede.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid NewUserDTO newUserDTO) {

        Optional<UserEntity> optionalUser = userService.findByEmail(newUserDTO.email());

        if (optionalUser.isPresent()) {
            throw new AlreadyExistsException("User with provided email: " + newUserDTO.email() + " already exists");
        }

        // Password must not be null when register a user
        if (newUserDTO.password() == null) {
            throw new IllegalArgumentException("Password must not be null");
        }

        AuthenticationResponse response = authenticationService.register(newUserDTO);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authenticationService.authenticate(request));
    }
}
