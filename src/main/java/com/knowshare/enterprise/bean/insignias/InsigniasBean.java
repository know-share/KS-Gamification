/**
 * 
 */
package com.knowshare.enterprise.bean.insignias;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.enterprise.repository.idea.IdeaRepository;
import com.knowshare.enterprise.repository.ludificacion.InsigniaRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.ludificacion.Insignia;
import com.knowshare.entities.ludificacion.InsigniaPreview;
import com.knowshare.entities.perfilusuario.Usuario;

/**
 * @author Miguel Montañez
 *
 */
@Component
public class InsigniasBean implements InsigniasFacade {
	
	@Autowired
	private IdeaRepository ideaRepository;
	
	@Autowired
	private InsigniaRepository insigniaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String IDEA_PREFIX = "IDEA";
	private static final String SEGUIDORES_PREFIX = "SEGUIDORES";
	private static final String AMIGOS_PREFIX = "AMIGOS";

	@Override
	public void insigniasCreacionIdea(IdeaDTO idea) {
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(idea.getUsuario());
		final long cantidadIdeas = ideaRepository.countByUsuario(usuario.getId());
		final Insignia insignia = insigniaRepository.findOne(IDEA_PREFIX+cantidadIdeas);
		if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuario.getInsignias().add(preview);
			usuarioRepository.save(usuario);
		}
	}
	
	@Override
	public void insigniasSeguir(String username) {
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Insignia insignia = insigniaRepository.findOne(SEGUIDORES_PREFIX+usuario.getSeguidores().size());
		if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuario.getInsignias().add(preview);
			usuarioRepository.save(usuario);
		}
	}
	
	@Override
	public void insigniasAccionSolicitud(String username) {
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Insignia insignia = insigniaRepository.findOne(AMIGOS_PREFIX+usuario.getAmigos().size());
		if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuario.getInsignias().add(preview);
			usuarioRepository.save(usuario);
		}
	}
	
	@Override
	public void insigniasCompartir(IdeaDTO idea) {
		if(idea.isCompartida()){
			final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(idea.getUsuario());
			final Insignia insignia = insigniaRepository.findOne(IDEA_PREFIX+"_COMPARTIR");
			if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
				final InsigniaPreview preview = new InsigniaPreview()
						.setInsignia(insignia)
						.setVisto(false);
				usuario.getInsignias().add(preview);
				usuarioRepository.save(usuario);
			}
		}
	}
	
	@Override
	public void insigniasAgregarTGDirigido(String username) {
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Insignia insignia = insigniaRepository.findOne("ADD_TG");
		if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuario.getInsignias().add(preview);
			usuarioRepository.save(usuario);
		}
	}
	
	@Override
	public void insigniasAgregarFormacionAcademica(String username) {
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Insignia insignia = insigniaRepository.findOne("ADD_FA");
		if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuario.getInsignias().add(preview);
			usuarioRepository.save(usuario);
		}
	}
	
	private boolean exist(List<InsigniaPreview> insignias,String insigniaId){
		return !insignias.stream()
				.filter(ins->ins.getInsignia().getId().equals(insigniaId))
				.collect(Collectors.toList()).isEmpty();
	}
}
