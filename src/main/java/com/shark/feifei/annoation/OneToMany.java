package com.shark.feifei.annoation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OneToMany {
    Class entity() default Object.class;
    String primaryKey() default "";
}
