package com.acedigital.smartresolve.repositories;

import com.acedigital.smartresolve.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically compiles this into a securely parameterized statement
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
