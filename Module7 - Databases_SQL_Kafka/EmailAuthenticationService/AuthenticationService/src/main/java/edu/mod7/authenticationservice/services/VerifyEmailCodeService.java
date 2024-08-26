package edu.mod7.authenticationservice.services;

import edu.mod7.authenticationservice.exceptions.VerifiedEmailNotFoundException;
import edu.mod7.authenticationservice.models.VerifyEmailCode;
import edu.mod7.authenticationservice.repositories.VerifyEmailCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerifyEmailCodeService {
    private final VerifyEmailCodeRepository repository;



    public void save(String email, String verifyCode) {
        repository.save(
                new VerifyEmailCode(email, verifyCode)
        );
    }

    public String getCodeByEmail(String email) {
        Optional<VerifyEmailCode> opt = repository.findByEmail(email);

        return opt.orElseThrow(
                VerifiedEmailNotFoundException::new
        ).getVerifyCode();
    }

    public void removeByEmail(String email) {
        repository.deleteByEmail(email);
    }
}