/**
 * 
 */
package com.knowshare.enterprise.bean.insignias;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.entities.ludificacion.Insignia;
import com.knowshare.entities.perfilusuario.Usuario;

/**
 * Dada la acción se revisa si el usuario actual o
 * el objetivo (en ciertos casos), se le debe otorgar
 * una {@link Insignia}.
 * @author Miguel Montañez
 *
 */
public interface InsigniasFacade {
	
	/**
	 * Otorga insignias cuando se crean 1, 10 y 50
	 * ideas
	 * @param idea creada.
	 */
	void insigniasCreacionIdea(IdeaDTO idea);

	/**
	 * Otorga insignias cuando obtiene 1, 10 y 50
	 * seguidores
	 * @param username del usuario seguido
	 */
	void insigniasSeguir(String username);
	
	/**
	 * Otorga insignias cuando agrega 1, 10 y 50
	 * amigos
	 * @param username del usuario a revisar
	 */
	void insigniasAccionSolicitud(String username);
	
	/**
	 * Otorga insignia cuando comparte por primera vez
	 * una idea
	 * @param idea compartida
	 */
	void insigniasCompartir(IdeaDTO idea);
	
	/**
	 * Otorga insignia por agregar el primer trabajo dirigido.
	 * @param username del usuario que realiza la acción
	 */
	void insigniasAgregarTGDirigido(String username);
	
	/**
	 * Otorga insignia por agregar la primera formación académica.
	 * @param username del usuario que realiza la acción
	 */
	void insigniasAgregarFormacionAcademica(String username);
	
	/**
	 * Otorga insignias a los usuarios que dan su primer aval (usernameGive),
	 * y a aquellos que reciben aval
	 * @param username usuario que recibe aval.
	 * @param usernameGive usuario que da aval.
	 */
	void insigniasAvalarUsuario(String username,String usernameGive);
	
	/**
	 * Otorga insignias por antiguedad de 1, 6 y 12 meses
	 * @param usuario a quién se le revisará
	 */
	void insigniasAntiguedad(Usuario usuario);
}
