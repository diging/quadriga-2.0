package edu.asu.diging.quadriga.aspect;

import java.util.Arrays;

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
    
    public static final String AUTH_PARAM = "authentication";
    public static final String TOKEN_PARAM = "tokenInfo";

    @Around("execution(@edu.asu.diging.quadriga.aspect.annotation.InjectToken * *.*(..))")
    public Object authAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int authIndex = getIndexOf(joinPoint, AUTH_PARAM);
        int tokenIndex = getIndexOf(joinPoint, TOKEN_PARAM);

        if (authIndex >= 0 && tokenIndex >= 0) {
            Authentication authentication = (Authentication) args[authIndex];
            if (authentication != null && authentication.getDetails() instanceof TokenInfo) {
                args[tokenIndex] = authentication.getDetails();
            }
        } else if (tokenIndex >= 0) {
            args[tokenIndex] = null;
        }
        return joinPoint.proceed(args);
    }

    private int getIndexOf(ProceedingJoinPoint joinPoint, String argName) {
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        return Arrays.asList(paramNames).indexOf(argName);
    }
}
