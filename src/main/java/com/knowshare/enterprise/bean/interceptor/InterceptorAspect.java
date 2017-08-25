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
import com.knowshare.entities.perfilusuario.Usuario;

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
	
	private static final String DEBUG_MSG = "KS-Gamification Interceptando método {}";
	
	@Autowired
	private InsigniasFacade insigniasBean;

	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.idea.IdeaModBean.crearIdea(..))",
			returning="result")
	public void afterCrearIdea(JoinPoint joinPoint,Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if(null != result)
			insigniasBean.insigniasCreacionIdea((IdeaDTO)result);
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.usuario.UsuarioModBean.seguir(..))",
			returning="result")
	public void afterSeguir(JoinPoint joinPoint,Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if((boolean)result)
			insigniasBean.insigniasSeguir((String)joinPoint.getArgs()[1]);
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.usuario.UsuarioModBean.accionSolicitud(..))",
			returning="result")
	public void afterAccionSolicitud(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if((boolean)result){
			insigniasBean.insigniasAccionSolicitud((String)joinPoint.getArgs()[0]);
			insigniasBean.insigniasAccionSolicitud((String)joinPoint.getArgs()[1]);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.idea.IdeaModBean.compartir(..))",
			returning="result")
	public void afterCompartir(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if(null != result)
			insigniasBean.insigniasCompartir((IdeaDTO)result);
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.usuario.UsuarioModBean.agregarTGDirigido(..))",
			returning="result")
	public void afterAgregarTGDirigido(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if((boolean)result)
			insigniasBean.insigniasAgregarTGDirigido((String)joinPoint.getArgs()[1]);
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.usuario.UsuarioModBean.agregarFormacionAcademica(..))",
			returning="result")
	public void afterAgregarFormacionAcademica(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if((boolean)result)
			insigniasBean.insigniasAgregarFormacionAcademica((String)joinPoint.getArgs()[1]);
	}
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.avales.AvalBean.avalarUsuario(..))",
			returning="result")
	public void afterAvalarUsuario(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if((boolean)result)
			insigniasBean.insigniasAvalarUsuario((String)joinPoint.getArgs()[1], (String)joinPoint.getArgs()[0]);
	}
	
	
	@AfterReturning(
			pointcut="execution(* com.knowshare.enterprise.bean.usuario.UsuarioListBean.login(..))",
			returning="result")
	public void afterLogin(JoinPoint joinPoint, Object result){
		logger.info(DEBUG_MSG,joinPoint.getSignature().getName());
		if(null != result)
			insigniasBean.insigniasAntiguedad((Usuario)result);
	}
}
