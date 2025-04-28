package de.koerperschmiede.backend.models.entities;

import de.koerperschmiede.backend.util.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tokens")
public class TokenEntity extends BaseEntity {
    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public UserEntity user;

    public static TokenEntity of(String token, TokenType tokenType, boolean revoked, boolean expired, UserEntity user) {
        return new TokenEntity(token, tokenType, revoked, expired, user);
    }
}
