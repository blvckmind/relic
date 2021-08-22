package io.github.blvckmind.relic.persistence.validator;

import io.github.blvckmind.relic.persistence.annotation.ValidName;
import io.github.blvckmind.relic.persistence.model.entity.PersonEntity;
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
