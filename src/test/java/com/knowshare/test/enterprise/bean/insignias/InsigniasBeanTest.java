/**
 * 
 */
package com.knowshare.test.enterprise.bean.insignias;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.bean.insignias.InsigniasFacade;
import com.knowshare.enterprise.repository.idea.IdeaRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.idea.Idea;
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
	
	private IdeaDTO dto;
	private Idea idea;
	
	@Before
	public void setup(){
		dto = new IdeaDTO()
				.setUsuario("MinMiguelM")
				.setTipo(TipoIdeaEnum.NU);
		
		idea = new Idea()
				.setUsuario(usuarioRepository.findByUsernameIgnoreCase("minmiguelm"))
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
	
	@AfterClass
	public static void tearDown(){
	}
}
