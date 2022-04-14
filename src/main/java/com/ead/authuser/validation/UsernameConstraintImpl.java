package com.ead.authuser.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameConstraintImpl implements ConstraintValidator<UsernameContraint, String> {

    @Override
    public void initialize(final UsernameContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext constraintValidatorContext) {

        return username != null && !username.trim().isEmpty() && !username.contains(" ");
    }
}
