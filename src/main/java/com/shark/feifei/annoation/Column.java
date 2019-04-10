package com.shark.feifei.annoation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Column {
    String value() default "";
    String name() default "";
}
