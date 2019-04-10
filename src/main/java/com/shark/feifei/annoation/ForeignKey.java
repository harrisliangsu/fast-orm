package com.shark.feifei.annoation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ForeignKey {
    Class entity() default Object.class;
    String column() default "";
}
