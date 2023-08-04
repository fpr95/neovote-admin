package com.digiteo.neovoteIV.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameArrayLengthValidator implements ConstraintValidator<UsernameMinLength, String[]> {

    private int minLength;

    @Override
    public void initialize(UsernameMinLength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minLength = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String[] array, ConstraintValidatorContext constraintValidatorContext) {
        for(String uname:array){
            if(uname == null || uname.length() < minLength){
                return false;
            }
        }
        return true;
    }
}
