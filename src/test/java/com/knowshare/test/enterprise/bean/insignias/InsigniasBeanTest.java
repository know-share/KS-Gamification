/**
 * 
 */
package com.knowshare.test.enterprise.bean.insignias;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.bean.insignias.InsigniasFacade;
import com.knowshare.enterprise.repository.idea.IdeaRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.idea.Idea;
import com.knowshare.entities.ludificacion.UsuarioAval;
import com.knowshare.entities.perfilusuario.InfoUsuario;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoIdeaEnum;
import com.knowshare.test.enterprise.general.AbstractTest;

/**
 * Pruebas para métodos unitarios en la asignación de 
 * insignias del sistema.
 * @author Miguel Montañez
 *
 */
public class InsigniasBeanTest extends AbstractTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private IdeaRepository ideaRepository;
	
	@Autowired
	private InsigniasFacade insigniasBean;
	
	private IdeaDTO dtoCompartida;
	private IdeaDTO dto;
	private Idea idea;
	
	@Before
	public void setup(){
		dto = new IdeaDTO()
				.setUsuario("MinMiguelM")
				.setCompartida(false)
				.setTipo(TipoIdeaEnum.NU);
		
		dtoCompartida = new IdeaDTO()
				.setUsuario("MinMiguelM")
				.setCompartida(true)
				.setTipo(TipoIdeaEnum.NU);
		
		idea = new Idea()
				.setUsuario(usuarioRepository.findByUsernameIgnoreCase("minmiguelm"))
				.setCompartida(false)
				.setTipo(TipoIdeaEnum.NU);
	}
	
	@Test
	public void insigniasCreacionIdeaTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasCreacionIdea(dto);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
		
		ideaRepository.insert(copyIdea(idea));
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasCreacionIdea(dto);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		ideaRepository.insert(copyIdea(idea));
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasCreacionIdea(dto);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasSeguirTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasSeguir("minmiguelm");
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
		
		usuario.getSeguidores().add(new InfoUsuario());
		usuarioRepository.save(usuario);
		insigniasBean.insigniasSeguir(usuario.getUsername());
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias+1, usuario.getInsignias().size());
		
		insigniasBean.insigniasSeguir(usuario.getUsername());
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias+1, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasAccionSolicitudTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAccionSolicitud("minmiguelm");
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
		
		usuario.getAmigos().add(new InfoUsuario());
		usuarioRepository.save(usuario);
		insigniasBean.insigniasAccionSolicitud(usuario.getUsername());
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias+1, usuario.getInsignias().size());
		
		for(int i = 1; i<10;i++)
			usuario.getAmigos().add(new InfoUsuario());
		
		usuario = usuarioRepository.save(usuario);
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAccionSolicitud(usuario.getUsername());
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias+1, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasCompartirTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasCompartir(dtoCompartida);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasCompartir(dtoCompartida);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasAgregarTGDirigidoTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAgregarTGDirigido("pablo.gaitan");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAgregarTGDirigido("pablo.gaitan");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasAgregarFormacionAcademicaTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAgregarFormacionAcademica("pablo.gaitan");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAgregarFormacionAcademica("pablo.gaitan");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasAvalarUsuarioTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		usuario.getPersonasAvaladas().add(new UsuarioAval());
		usuarioRepository.save(usuario);
		
		int insignias = usuario.getInsignias().size();
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insigniasMiguel = usuario.getInsignias().size();
		insigniasBean.insigniasAvalarUsuario("minmiguelm","pablo.gaitan");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insigniasMiguel + 1, usuario.getInsignias().size());
		
		usuario.getPersonasAvaladas().add(new UsuarioAval());
		usuarioRepository.save(usuario);
		
		insigniasMiguel = usuario.getInsignias().size();
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		insignias = usuario.getInsignias().size();
		insigniasBean.insigniasAvalarUsuario("pablo.gaitan","minmiguelm");
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insigniasMiguel + 1, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasAntiguedadTest(){
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		int insignias = usuario.getInsignias().size();
		usuario.setFechaRegistro(getDate(2));
		usuario = usuarioRepository.save(usuario);
		
		insigniasBean.insigniasAntiguedad(usuario);
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario.setFechaRegistro(getDate(7));
		insignias = usuario.getInsignias().size();
		usuario = usuarioRepository.save(usuario);
		
		insigniasBean.insigniasAntiguedad(usuario);
		usuario = usuarioRepository.findByUsernameIgnoreCase("pablo.gaitan");
		assertEquals(insignias + 1, usuario.getInsignias().size());
		
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		insignias = usuario.getInsignias().size();
		usuario.setFechaRegistro(getDate(12));
		usuario = usuarioRepository.save(usuario);
		
		insigniasBean.insigniasAntiguedad(usuario);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias + 3, usuario.getInsignias().size());
	}
	
	@Test
	public void insigniasLeaderBoardTest(){
		final List<LeaderDTO> leader = new ArrayList<>();
		leader.add(new LeaderDTO().setUsername("pablo.gaitan"));
		
		Usuario usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		int insignias = usuario.getInsignias().size();
		insigniasBean.insigniasLeaderBoard("minmiguelm", leader);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias, usuario.getInsignias().size());
		
		leader.add(new LeaderDTO().setUsername("minmiguelm"));
		insigniasBean.insigniasLeaderBoard("minmiguelm", leader);
		usuario = usuarioRepository.findByUsernameIgnoreCase("minmiguelm");
		assertEquals(insignias+1, usuario.getInsignias().size());
	}
	
	private Date getDate(int months){
		final Date date = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.MONTH, months*-1);
		return c.getTime();
	}
	
	private Idea copyIdea(Idea idea){
		return new Idea()
				.setAlcance(idea.getAlcance())
				.setComentarios(idea.getComentarios())
				.setCompartida(idea.isCompartida())
				.setContenido(idea.getContenido())
				.setUsuario(idea.getUsuario())
				.setEstado(idea.getEstado())
				.setFechaCreacion(idea.getFechaCreacion())
				.setTipo(idea.getTipo());
	}
}
