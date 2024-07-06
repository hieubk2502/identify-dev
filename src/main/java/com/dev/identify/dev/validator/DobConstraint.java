package com.dev.identify.dev.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // ap dung khi dung voi value
@Retention(RetentionPolicy.RUNTIME) // khi chay trong runtime
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min(); // do tuoi nho nhat

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
