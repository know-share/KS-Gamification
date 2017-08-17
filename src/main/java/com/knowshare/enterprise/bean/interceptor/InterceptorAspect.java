/**
 * 
 */
package com.knowshare.enterprise.bean.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.insignias.InsigniasFacade;

/**
 * Interceptor encargado de mirar si hay que otorgar insignias
 * debido a cierta acción.
 * @author Miguel Montañez
 *
 */
@Aspect
@Component
public class InterceptorAspect {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private InsigniasFacade insigniasBean;

	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.idea.IdeaModBean.crearIdea(..))",
			returning="result")
	public void afterCrearIdea(JoinPoint joinPoint,Object result){
		logger.info("KS-Gamification Interceptando método {}",joinPoint.getSignature().getName());
		if(null != result)
			insigniasBean.insigniasIdea((IdeaDTO)result);
	}
}
