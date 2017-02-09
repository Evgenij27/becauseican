package org.nashorn.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Path {

    String path() default "";
    String method() default "";
}
