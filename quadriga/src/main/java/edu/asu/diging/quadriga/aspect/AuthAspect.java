package edu.asu.diging.quadriga.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.asu.diging.quadriga.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.exceptions.TokenNotFoundException;

/**
 * Aspect configuration class injects the token info from citesphere into the appropriate method parameter
 * @author Maulik Limbadiya
 *
 */
@Aspect
@Configuration
public class AuthAspect {

    @Pointcut("execution(@edu.asu.diging.quadriga.aspect.annotation.InjectToken * *(..))")
    public void annotatedMethod() {}

    @Pointcut("execution(* *(.., edu.asu.diging.quadriga.config.web.TokenInfo, ..))")
    public void tokenParamMethod() {}

    /**
     * Injects token info from the security context into the method parameter of same class type.
     * This aspect is wired for the method annotated with {@link InjectToken} and has a method parameter of type {@link TokenInfo}
     * @param joinPoint Contains data regarding the method signature and arguments passed
     * @return Updated method args containing the token info
     */
    @Around("annotatedMethod() && tokenParamMethod()")
    public Object authAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int tokenIndex = getIndexOf(args, TokenInfo.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof TokenInfo) {
            args[tokenIndex] = authentication.getDetails();
        } else {
            throw new TokenNotFoundException("No token info found to retrieve app client id");
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
