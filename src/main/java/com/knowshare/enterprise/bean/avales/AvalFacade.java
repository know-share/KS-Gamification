/**
 * 
 */
package com.knowshare.enterprise.bean.avales;

import org.bson.types.ObjectId;

import com.knowshare.enums.TipoAvalEnum;

/**
 * Bean encargado de manejar los avales de los usuarios
 * de la aplicación
 * @author Miguel Montañez
 *
 */
public interface AvalFacade {

	/**
	 * Permite avalar la cualidad o habilidad de un usuario dado.
	 * @param username del usuario que ejecuta la acción.
	 * @param usernameTarget del usuario al cual se le avalará 
	 * la habilidad/cualidad.
	 * @param id de la habilidad o cualidad
	 * @param tipo {@link TipoAvalEnum}}
	 * @return falso si ya anteriormente había avalado o si hubo problemas
	 *  de lo contrario, verdadero.
	 */
	boolean avalarUsuario(String username, String usernameTarget,ObjectId id, TipoAvalEnum tipo);
}
