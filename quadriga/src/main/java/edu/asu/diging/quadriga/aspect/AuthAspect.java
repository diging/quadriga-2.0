package edu.asu.diging.quadriga.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import edu.asu.diging.quadriga.config.web.TokenInfo;

@Aspect
@Configuration
public class AuthAspect {

    @Pointcut("execution(@edu.asu.diging.quadriga.aspect.annotation.InjectToken * *(..))")
    public void annotatedMethod() {}

    @Pointcut("execution(* *(.., edu.asu.diging.quadriga.config.web.TokenInfo, ..))")
    public void tokenParamMethod() {}

    @Around("annotatedMethod() && tokenParamMethod()")
    public Object authAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int authIndex = getIndexOf(args, Authentication.class);
        int tokenIndex = getIndexOf(args, TokenInfo.class);

        if (tokenIndex >= 0) {
            args[tokenIndex] = null;
            if (authIndex >= 0) {
                Authentication authentication = (Authentication) args[authIndex];
                if (authentication.getDetails() instanceof TokenInfo) {
                    args[tokenIndex] = authentication.getDetails();
                }
            }

            if (args[tokenIndex] == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        return joinPoint.proceed(args);
    }

    private int getIndexOf(Object[] args, Class<?> classType) {
        for (int i = 0; i < args.length; i++) {
            if (classType.isAssignableFrom(args[i].getClass())) {
                return i;
            }
        }
        return -1;
    }
}
