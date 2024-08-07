package edu.mod6.linkabbreviationsservice.annotations;

import edu.mod6.linkabbreviationsservice.annotations.validators.UniqueSourceLinkValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Аннотация для проверки что переданная ссылка еще не занята (не содержится в базе данных)
//  todo нету отдельной возможность проверить что не нарушается уникальность вставленных значений?

@Constraint(validatedBy = UniqueSourceLinkValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSourceLink {

    String message() default "This link is already in database!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
