package com.dev.identify.dev.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;


    // xay ra truoc buoc method isValid
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);

        min = constraintAnnotation.min(); // get value min age (get value from @interface DobConstraint)
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(localDate))
            return true;

        long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());

        return years >= min;
    }
}
