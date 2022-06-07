package edu.asu.diging.quadriga.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import edu.asu.diging.quadriga.config.web.TokenInfo;

@Aspect
@Configuration
public class AuthAspect {

    @Pointcut("execution(@edu.asu.diging.quadriga.aspect.annotation.InjectToken * *(..))")
    public void annotatedMethod() {}

    @Pointcut("execution(* *(.., org.springframework.security.core.Authentication, ..))")
    public void authenticationParamMethod() {}

    @Pointcut("execution(* *(.., edu.asu.diging.quadriga.config.web.TokenInfo, ..))")
    public void tokenParamMethod() {}

    @Around("annotatedMethod() && authenticationParamMethod() && tokenParamMethod()")
    public Object authAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int authIndex = getIndexOf(args, Authentication.class);
        int tokenIndex = getIndexOf(args, TokenInfo.class);

        if (authIndex >= 0 && tokenIndex >= 0) {
            Authentication authentication = (Authentication) args[authIndex];
            if (authentication != null && authentication.getDetails() instanceof TokenInfo) {
                args[tokenIndex] = authentication.getDetails();
            } else {
                args[tokenIndex] = null;
            }
        } else if (tokenIndex >= 0) {
            args[tokenIndex] = null;
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
