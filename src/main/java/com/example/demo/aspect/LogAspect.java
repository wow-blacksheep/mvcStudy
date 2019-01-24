package com.example.demo.aspect;

import com.example.demo.annotation.BatchProcessAnnotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.example.demo.service.impl.*ServiceImpl.*(..))")
    public void logPointcut() {

    }

    @Before("logPointcut()")
    public void logBeforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println("getArgs:"+ Arrays.toString(args));

        Object pointThis = joinPoint.getThis();
        System.out.println("getThis:"+pointThis);

        Object target = joinPoint.getTarget();
        System.out.println("getTarget:"+target);

        String kind = joinPoint.getKind();
        System.out.println("getKind:"+kind);

        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        String name = signature.getName();
        System.out.println("getName:"+name);

        Method method = signature.getMethod();
        System.out.println("getMethod:"+method);

        Class returnType = signature.getReturnType();
        System.out.println("getReturnType:"+returnType);
    }

    @Pointcut("@annotation(com.example.demo.annotation.BatchProcessAnnotation)")
    public void batchProcessPointcut() {

    }

    @AfterReturning("batchProcessPointcut()")
    public void batchProcessAfterAdvice(JoinPoint joinPoint) throws NoSuchMethodException {

/*        //这种情况下，CGLIB代理方式会报错(proxy-target-class为true)，因为拿不到代理上的注解，但是JDK Proxy代理方式可以
        BatchProcessAnnotation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(BatchProcessAnnotation.class);
        String value = annotation.value();
        System.out.println(value);*/

       /* //这种情况下，JDK Proxy代理方式会报错(proxy-target-class为false)，因为拿不到代理上的注解，但是CGLIB代理方式可以
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        BatchProcessAnnotation annotation = method.getAnnotation(BatchProcessAnnotation.class);
        String value = annotation.value();
        System.out.println(value);*/

        // 这种方式满足两种代理方式，因为取的是目标类，采用基本反射获取
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();

        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(signature.getName(),  signature.getMethod().getParameterTypes());

        BatchProcessAnnotation annotation = method.getAnnotation(BatchProcessAnnotation.class);
        String value = annotation.value();
        System.out.println(value);
    }
}