package edu.asu.diging.quadriga.core.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import edu.asu.diging.quadriga.config.web.TokenInfo;
import edu.asu.diging.quadriga.core.aspect.annotation.InjectToken;
import edu.asu.diging.quadriga.core.aspect.annotation.VerifyCollectionAccess;
import edu.asu.diging.quadriga.core.exceptions.TokenNotFoundException;
import edu.asu.diging.quadriga.core.model.Collection;
import edu.asu.diging.quadriga.core.service.CollectionManager;

/**
 * Aspect configuration class injects the token info from citesphere into the appropriate method parameter
 * @author Maulik Limbadiya
 *
 */
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private CollectionManager collectionManager;
    
    @Pointcut("execution(@edu.asu.diging.quadriga.core.aspect.annotation.InjectToken * *(..))")
    public void injectTokenAnnotatedMethod() {}

    @Pointcut("execution(@edu.asu.diging.quadriga.core.aspect.annotation.VerifyCollectionAccess * *(..))")
    public void verifyCollectionAccessAnnotatedMethod() {}
    
    @Pointcut("execution(* *(.., edu.asu.diging.quadriga.config.web.TokenInfo, ..))")
    public void tokenParamMethod() {}

    /**
     * Injects token info from the security context into the method parameter of same class type.
     * This aspect is wired for the method annotated with {@link InjectToken} and has a method parameter of type {@link TokenInfo}
     * @param joinPoint Contains data regarding the method signature and arguments passed
     * @return Updated method args containing the token info
     */
    @Order(1)
    @Around("injectTokenAnnotatedMethod() && tokenParamMethod()")
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
    
    /**
     * Verifies if the app has access to the collection.
     * This aspect is wired for the method annotated with {@link InjectToken}, {@link VerifyCollectionAccess} and has a method parameter of type {@link TokenInfo}
     * @param joinPoint Contains data regarding the method signature and arguments passed
     */
    @Order(2)
    @Before("injectTokenAnnotatedMethod() && verifyCollectionAccessAnnotatedMethod() && tokenParamMethod()")
    public void verifyCollectionAccessAdvice(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String collectionParamName = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(VerifyCollectionAccess.class).collectionParam();
        
        int tokenIndex = getIndexOf(args, TokenInfo.class);
        int collectionIdIndex = getIndexOf(joinPoint, collectionParamName);
        
        TokenInfo tokenInfo = (TokenInfo) args[tokenIndex];
        String collectionId = (String) args[collectionIdIndex];
        
        Collection collection = collectionManager.findCollection(collectionId);
        if (collection.getApps() == null || collection.getApps().isEmpty()
                || !collection.getApps().contains(tokenInfo.getClient_id())) {
            throw new BadCredentialsException(tokenInfo.getClient_id() + " app cannot access collection " + collectionId);
        }
        
    }

    private int getIndexOf(Object[] args, Class<?> classType) {
        for (int i = 0; i < args.length; i++) {
            if (classType.isAssignableFrom(args[i].getClass())) {
                return i;
            }
        }
        return -1;
    }
    
    private int getIndexOf(JoinPoint joinPoint, String parameterName) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] paramNames = codeSignature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(parameterName)) {
                return i;
            }
        }
        return -1;
    }
}
