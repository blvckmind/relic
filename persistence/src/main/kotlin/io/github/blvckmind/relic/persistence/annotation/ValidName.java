package io.github.blvckmind.relic.persistence.annotation;

import io.github.blvckmind.relic.persistence.validator.PersonNameValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PersonNameValidator.class)
@Documented
public @interface ValidName {

}
