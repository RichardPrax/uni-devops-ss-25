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

    // TODO: Vor und Nachteile von FetchType.EAGER diskutieren und schauen was wir nehmen wollen/sollten
    /*
        Eager loading is a strategy in which the related entities are loaded immediately with the main entity.
        This means that when you fetch a UserEntity, all the related TrainingPlanEntity and TrainingSessionEntity objects are also fetched at the same time.
        This can be beneficial if you know that you will need the related entities right away, as it can reduce the number of database queries.
        However, it can also lead to performance issues if the related entities are large or if there are many of them, as it can result in loading a lot of unnecessary data.
        Lazy loading, on the other hand, is a strategy in which the related entities are loaded only when they are accessed for the first time.
        This means that when you fetch a UserEntity, only the UserEntity is loaded, and the related TrainingPlanEntity and TrainingSessionEntity objects are not loaded until you try to access them.
        This can be beneficial if you don't always need the related entities, as it can reduce the amount of data loaded into memory.

        Es gab das Problem bei den tests, das user.getTrainingPlans().stream() nicht funktioniert hat, weil die Liste nicht initialisiert war.
        Das gleiche gilt für für die anderen Objekte, da müssen wir überall nochmal schauen
    */
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

    public static UserEntity of(String firstName, String lastName, String email, String password, LocalDate birthdate, List<TrainingPlanEntity> trainingPlans, List<TrainingSessionEntity> sessions, Role role, List<TokenEntity> tokens) {
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
