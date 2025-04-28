package de.koerperschmiede.backend.repositories;

import de.koerperschmiede.backend.models.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<TokenEntity, UUID> {

    @Query(value = """
        select t from tokens t inner join users u\s
        on t.user.id = u.id\s
        where u.id = :id and (t.expired = false or t.revoked = false)\s
        """)
    List<TokenEntity> findAllValidTokenByUser(UUID id);

    Optional<TokenEntity> findByToken(String token);
}
