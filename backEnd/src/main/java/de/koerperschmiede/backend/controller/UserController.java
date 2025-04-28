package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.mapping.Mapper;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.dto.out.UserDTO;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.services.AuthenticationService;
import de.koerperschmiede.backend.services.UserService;
import de.koerperschmiede.backend.util.Role;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserEntity> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
            .body(users
                .stream()
                .map(Mapper::userEntityToUserDTO)
                .collect(Collectors.toList()));
    }

    // TODO: should we just return the user from the AuthenticationContext if it is not an admin?
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {

            var optionalUser = userService.findById(id);

            if (optionalUser.isEmpty()) {
                throw new NoSuchElementException("User with id: " + id + " not found");
            }

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.userEntityToUserDTO(optionalUser.get()));
        }

        if (!(user.getId().equals(id))) {
            throw new AccessException("You do not have permission to access this resource");
        }

        // we do not need to check if the user exists, because we are getting information about the logged-in user
        var optionalUser = userService.findById(id);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.userEntityToUserDTO(optionalUser.get()));
    }

    @GetMapping(path = "/email")
    public ResponseEntity<?> findByEmail(@RequestParam String email) {
        Optional<UserEntity> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("User with email: " + email + "not found");
        }

        UserEntity user = optionalUser.get();
        authenticationService.verifyUserAccess(user.getId());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.userEntityToUserDTO(user));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid NewUserDTO newUserDTO) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var updatedUser = userService.update(id, newUserDTO);

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.userEntityToUserDTO(updatedUser));
        }

        if (!(user.getId().equals(id))) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var updatedUser = userService.update(id, newUserDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.userEntityToUserDTO(updatedUser));
    }

    /**
     * This method is only accessible for users with the role 'ADMIN'
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
