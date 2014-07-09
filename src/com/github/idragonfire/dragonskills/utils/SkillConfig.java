package com.github.idragonfire.dragonskills.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SkillConfig {
    int min() default 0;

    int max() default Integer.MAX_VALUE;
}
