package edu.mod6.linkabbreviationsservice.annotations.validators;

import edu.mod6.linkabbreviationsservice.annotations.UniqueAllies;
import edu.mod6.linkabbreviationsservice.repositories.LinkAlliesRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UniqueAlliesValidator implements ConstraintValidator<UniqueAllies, Set<String>> {
    private final LinkAlliesRepository repository;

    @Override
    public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
        for (String ally: value) {
            if (repository.existsByAllyName(ally))
                return false;
        }

        return true;
    }
}