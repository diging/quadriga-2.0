package edu.asu.diging.quadriga.core.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation to extract the Authentication Token Info and injecting into method variable.
 * The method requires TokenInfo as one of the method parameter.
 * @author Maulik Limbadiya
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InjectToken {

}
