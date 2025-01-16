package edu.asu.diging.quadriga.core.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation which verifies if the Citesphere App corresponding to
 * the authentication token has access to the collection. To be used in
 * conjunction with {@link InjectToken}. The method requires TokenInfo and
 * String containing collectionId as method parameters.
 * 
 * @author Maulik Limbadiya
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VerifyCollectionAccess {
    public String collectionParam() default "collectionId";
}
