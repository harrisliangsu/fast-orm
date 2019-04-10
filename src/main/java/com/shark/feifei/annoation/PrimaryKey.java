package com.shark.feifei.annoation;

import java.lang.annotation.*;

/**
 * Indicate primary key to a field. A entity only can have one PrimaryKey.
 * @Author: Shark Chili
 * @Date: 2018/10/17 0017
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PrimaryKey {
}
