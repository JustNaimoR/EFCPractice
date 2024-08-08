package edu.mod6.linkabbreviationsservice.annotations;

import edu.mod6.linkabbreviationsservice.annotations.validators.UniqueAlliesValidator;
import edu.mod6.linkabbreviationsservice.annotations.validators.UniqueSourceLinkValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Проверка что все ally названия еще не заняты

@Constraint(validatedBy = UniqueAlliesValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface UniqueAllies {

    String message() default "This ally is already in database!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}