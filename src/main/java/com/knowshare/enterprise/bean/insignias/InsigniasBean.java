/**
 * 
 */
package com.knowshare.enterprise.bean.insignias;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.dto.idea.IdeaDTO;
import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.repository.idea.IdeaRepository;
import com.knowshare.enterprise.repository.ludificacion.InsigniaRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.ludificacion.Insignia;
import com.knowshare.entities.ludificacion.InsigniaPreview;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoUsuariosEnum;

/**
 * {@link InsigniasFacade}
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
	private static final String AVALAR_PREFIX = "AVALAR";
	private static final String AVALADO_PREFIX = "AVALADO";
	private static final String ANTIGUEDAD_PREFIX = "ANTIGUEDAD";

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

	@Override
	public void insigniasAvalarUsuario(String username,String usernameGive) {
		// Mirar si fue primer aval dado.
		final Usuario usuarioGive = usuarioRepository.findByUsernameIgnoreCase(usernameGive);
		Insignia insignia = insigniaRepository.findOne(AVALAR_PREFIX);
		if(null != insignia && !exist(usuarioGive.getInsignias(),insignia.getId()) &&
				usuarioGive.getPersonasAvaladas().size() == 1){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuarioGive.getInsignias().add(preview);
			usuarioRepository.save(usuarioGive);
		}

		final Usuario usuarioRec = usuarioRepository.findByUsernameIgnoreCase(username);
		
		// Mirar primer aval recibido
		if(usuarioGive.getTipo().equals(TipoUsuariosEnum.PROFESOR) && 
				usuarioRec.getTipo().equals(TipoUsuariosEnum.ESTUDIANTE))
			insignia = insigniaRepository.findOne(AVALADO_PREFIX+"_PROFESOR");
		else
			insignia = insigniaRepository.findOne(AVALADO_PREFIX+"_OTRO");
		
		if(null != insignia && !exist(usuarioRec.getInsignias(),insignia.getId())){
			final InsigniaPreview preview = new InsigniaPreview()
					.setInsignia(insignia)
					.setVisto(false);
			usuarioRec.getInsignias().add(preview);
			usuarioRepository.save(usuarioRec);
		}
	}
	
	@Override
	public void insigniasAntiguedad(Usuario usuario){
		if(!usuario.getTipo().equals(TipoUsuariosEnum.ADMIN)){
			final Date date = new Date();
			final Long months = Duration.between(usuario.getFechaRegistro().toInstant(), date.toInstant())
					.toDays()/30;
			final List<String> badges = new ArrayList<>(); 
			if(months >= 1)
				badges.add("1");
			if(months >= 6)
				badges.add("6");
			if(months >= 12)
				badges.add("12");
			
			for (String string : badges) {
				final Insignia insignia = insigniaRepository.findOne(ANTIGUEDAD_PREFIX+string);
				if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
					final InsigniaPreview preview = new InsigniaPreview()
							.setInsignia(insignia)
							.setVisto(false);
					usuario.getInsignias().add(preview);
					usuarioRepository.save(usuario);
				}
			}
		}
	}
	
	@Override
	public void insigniasLeaderBoard(String username, List<LeaderDTO> leaderboard) {
		final List<LeaderDTO> leader = leaderboard
				.stream()
				.filter(l -> l.getUsername().equalsIgnoreCase(username))
				.collect(Collectors.toList());
		if(!leader.isEmpty()){
			final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
			final Insignia insignia = insigniaRepository.findOne("TOP3");
			if(null != insignia && !exist(usuario.getInsignias(),insignia.getId())){
				final InsigniaPreview preview = new InsigniaPreview()
						.setInsignia(insignia)
						.setVisto(false);
				usuario.getInsignias().add(preview);
				usuarioRepository.save(usuario);
			}
		}
	}
	
	/**
	 * Se encarga de revisar si un usuario tiene ya una insignia.
	 * @param insignias que un usuario posee.
	 * @param insigniaId id de la insignia a revisar
	 * @return verdadero si existe, falso si no.
	 */
	private boolean exist(List<InsigniaPreview> insignias,String insigniaId){
		return !insignias.stream()
				.filter(ins->ins.getInsignia().getId().equals(insigniaId))
				.collect(Collectors.toList()).isEmpty();
	}
}
