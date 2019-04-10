package com.shark.feifei.annoation;

import java.lang.annotation.*;

/**
 * Indicate union primary to a field. A entity can have multi union primary,represent by foreign key.
 * @Author: Shark Chili
 * @Date: 2018/10/17 0017
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UnionPrimaryKey {
}
