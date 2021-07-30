package io.github.blvckmind.relic.validator;

import io.github.blvckmind.relic.annotation.ValidName;
import io.github.blvckmind.relic.domain.entity.PersonEntity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonNameValidator implements ConstraintValidator<ValidName, PersonEntity> {

    @Override
    public boolean isValid(PersonEntity personEntity, ConstraintValidatorContext constraintContext) {
        if (personEntity == null) {
            return true;
        }

        return personEntity.getFirstName() != null || personEntity.getLastName() != null;
    }
}
