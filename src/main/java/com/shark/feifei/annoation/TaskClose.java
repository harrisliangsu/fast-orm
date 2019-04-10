package com.shark.feifei.annoation;

import java.lang.annotation.*;

/**
 * Indicate task is closed
 * @Author: Shark Chili
 * @Date: 2018/11/19 0019
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TaskClose {
}
