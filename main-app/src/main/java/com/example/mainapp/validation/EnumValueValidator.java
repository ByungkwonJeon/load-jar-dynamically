package com.bakkt.common.validator;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValidator, String> {

  List<String> valueList = null;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    if (value != null) {
      return valueList.stream().anyMatch(value::equalsIgnoreCase);
    } else {
      return true;
    }
  }

  @Override
  public void initialize(EnumValidator constraintAnnotation) {
    valueList = new ArrayList<String>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
      valueList.add(enumVal.toString());
    }
  }
}
