/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;
import java.util.List;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enums.TipoUsuariosEnum;

/**
 * Permite la generación de los leaderboard de la aplicación
 * @author Felipe Bautista
 *
 */
public interface LeaderFacade {

	/**
	 * Genera el leaderboard por inscritos en cada
	 * carrera.
	 * @return Lista de {@link LeaderDTO carreras}
	 */
	List<LeaderDTO> carrerasLeader();
	List<LeaderDTO> usuariosLeader(String username, String carrera, TipoUsuariosEnum tipo);
}
