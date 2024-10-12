package com.colorswitch.game;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(CLASS)
@Target({ METHOD, CONSTRUCTOR })
/**
 * Used to indicate that a method or a constructor is used for debugging.
 */
public @interface Debug {
}
