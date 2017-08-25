/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;
import java.util.List;

import com.knowshare.dto.ludificacion.CarreraLeaderDTO;

/**
 * @author Pipe
 *
 */
public interface LeaderFacade {

	List<CarreraLeaderDTO> carrerasLeader();
}
