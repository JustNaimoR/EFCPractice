package edu.mod6.linkabbreviationsservice.annotations.validators;

import edu.mod6.linkabbreviationsservice.annotations.UniqueSourceLink;
import edu.mod6.linkabbreviationsservice.repositories.LinksPairRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueSourceLinkValidator implements ConstraintValidator<UniqueSourceLink, String>  {
    private final LinksPairRepository repository;

    @Override
    public boolean isValid(String srcLink, ConstraintValidatorContext context) {
        return repository.findBySrcLink(srcLink).isEmpty();
    }
}