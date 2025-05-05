package de.koerperschmiede.backend.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.koerperschmiede.backend.util.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<TrainingPlanEntity> trainingPlans;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<TrainingSessionEntity> sessions;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @JsonIgnore
    private List<TokenEntity> tokens;

    public UserEntity(String firstName, String lastName, String email, String password, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
    }

    public static UserEntity of(String firstName,
        String lastName,
        String email,
        String password,
        LocalDate birthdate,
        List<TrainingPlanEntity> trainingPlans,
        List<TrainingSessionEntity> sessions,
        Role role,
        List<TokenEntity> tokens) {
        return new UserEntity(firstName, lastName, email, password, birthdate, trainingPlans, sessions, role, tokens);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
