/**
 * 
 */
package com.knowshare.test.enterprise.bean.leaderboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.bean.leaderboard.LeaderFacade;
import com.knowshare.enums.TipoUsuariosEnum;
import com.knowshare.test.enterprise.general.AbstractTest;

/**
 * @author Miguel Montañez
 *
 */
public class LeaderBeanTest extends AbstractTest{
	
	@Autowired
	private LeaderFacade leaderBean;
	
	@Test
	public void carrerasLeaderTest(){
		final List<LeaderDTO> leaderCarreras = leaderBean.carrerasLeader();
		assertNotNull(leaderCarreras);
		assertEquals(2, leaderCarreras.size());
		assertEquals(2, leaderCarreras.get(0).getCantidad());
		assertEquals(0, leaderCarreras.get(1).getCantidad());
	}

	@Test
	public void usuariosLeaderTest(){
		List<LeaderDTO> leaderUsuarios = leaderBean
				.usuariosLeader("pablo.gaitan", "idCarreraSistemas", TipoUsuariosEnum.PROFESOR);
		assertNotNull(leaderUsuarios);
		assertEquals(1,leaderUsuarios.size());
		assertEquals("pablo.gaitan",leaderUsuarios.get(0).getUsername());

		leaderUsuarios = leaderBean
				.usuariosLeader("minmiguelm", "idCarreraSistemas", TipoUsuariosEnum.ESTUDIANTE);
		assertNotNull(leaderUsuarios);
		assertEquals(1,leaderUsuarios.size());
		assertEquals("MinMiguelM",leaderUsuarios.get(0).getUsername());
		
		leaderUsuarios = leaderBean
				.usuariosLeader("minmiguelm", "idCarreraCivil", TipoUsuariosEnum.ESTUDIANTE);
		assertNotNull(leaderUsuarios);
		assertEquals(0,leaderUsuarios.size());
	}
}
