package com.banco.log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Logs {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	@AfterThrowing(pointcut = "execution(* com.banco..*.*(..))", throwing = "ex")
	public void logException(JoinPoint joinPoint, Throwable ex) {
		logger.error("Error {}() de la clase {}: {}", 
				joinPoint.getSignature().getName(),
				joinPoint.getTarget().getClass().getName(),
				ex.getMessage() + "\n" + ex.getStackTrace());
	}
	
	
    @Before("execution(* com.banco..*.*(..))")
    public void logMetodoEntrada(JoinPoint joinPoint) {
        logger.info("Método {}() de la clase {}", 
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }
    
    @AfterReturning("execution(* com.banco..*.*(..))")
    public void logMethodoSalida(JoinPoint joinPoint) {
        logger.info("Saliendo {}() de la clase {}", 
                     joinPoint.getSignature().getName(),
                     joinPoint.getTarget().getClass().getName());
    }

}