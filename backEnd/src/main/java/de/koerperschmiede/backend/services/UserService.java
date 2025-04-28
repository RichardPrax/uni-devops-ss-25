package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.exceptions.AlreadyExistsException;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findById(UUID userID) {
        return userRepository.findById(userID);
    }

    public Optional<UserEntity> findByEmail(String mail) {
        return userRepository.findByEmail(mail);
    }

    public UserEntity update(UUID userId, NewUserDTO newUserDTO) {

        UserEntity user = getUser(userId);

        if (isEmailAlreadyTaken(userId, newUserDTO)) {
            throw new AlreadyExistsException("Email: " + newUserDTO.email() + " is already in use");
        }

        updateUserDetails(newUserDTO, user);

        return userRepository.save(user);
    }

    private void updateUserDetails(NewUserDTO newUserDTO, UserEntity user) {
        user.setFirstName(newUserDTO.firstName());
        user.setLastName(newUserDTO.lastName());
        user.setEmail(newUserDTO.email());
        user.setBirthdate(newUserDTO.birthdate());
    }

    private boolean isEmailAlreadyTaken(UUID userId, NewUserDTO newUserDTO) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(newUserDTO.email());
        return existingUser.isPresent() && !existingUser.get().getId().equals(userId);
    }

    public void deleteById(UUID userID) {
        userRepository.deleteById(userID);
    }

    private UserEntity getUser(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User with id: " + id + " not found"));
    }
}
