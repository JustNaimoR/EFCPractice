package edu.mod7.authenticationservice.repositories;

import edu.mod7.authenticationservice.models.VerifyEmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyEmailCodeRepository extends JpaRepository<VerifyEmailCode, String> {
    Optional<VerifyEmailCode> findByEmail(String email);
    void deleteByEmail(String email);
}