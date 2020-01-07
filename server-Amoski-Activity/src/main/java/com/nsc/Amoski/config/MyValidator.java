package com.nsc.Amoski.config;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/*@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullValidator.class)*/


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(MyValidator.List.class)
@Documented
@Constraint(
        validatedBy = NotNullValidator.class
)
public @interface MyValidator {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name();

    String ColumnName() default "";

    boolean NotNull() default false;

    int min() default -1;

    int max() default -1;

    int lengthMin() default -1;

    int lengthMax() default -1;

}
