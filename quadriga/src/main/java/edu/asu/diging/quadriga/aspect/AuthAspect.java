package edu.asu.diging.quadriga.aspect;

import java.util.Arrays;

import javax.security.sasl.AuthenticationException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import edu.asu.diging.quadriga.config.web.TokenInfo;

@Aspect
@Configuration
public class AuthAspect {

    @Around("execution(* edu.asu.diging.quadriga.api.v1.GetCollectionsApiController.getCollections(..))")
    public Object authAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int authIndex = getIndexOf(joinPoint, "authentication");
        int tokenIndex = getIndexOf(joinPoint, "tokenInfo");

        if (authIndex >= 0 && tokenIndex >= 0) {
            Authentication authentication = (Authentication) args[authIndex];
            if (authentication.getDetails() instanceof TokenInfo) {
                args[tokenIndex] = authentication.getDetails();
            }
            return joinPoint.proceed(args);
        } else {
            throw new AuthenticationException();
        }
    }

    private int getIndexOf(ProceedingJoinPoint joinPoint, String argName) {
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        return Arrays.asList(paramNames).indexOf(argName);
    }
}
