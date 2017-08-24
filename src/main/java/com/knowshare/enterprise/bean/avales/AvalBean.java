/**
 * 
 */
package com.knowshare.enterprise.bean.avales;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.enterprise.repository.perfilusuario.CualidadRepository;
import com.knowshare.enterprise.repository.perfilusuario.HabilidadRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.ludificacion.UsuarioAval;
import com.knowshare.entities.perfilusuario.Cualidad;
import com.knowshare.entities.perfilusuario.Habilidad;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoAvalEnum;
import com.knowshare.enums.TipoUsuariosEnum;

/**
 * @author Miguel Montañez
 *
 */
@Component
public class AvalBean implements AvalFacade {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CualidadRepository cualidadRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	/**
	 * Método incompleto 
	 * @param username
	 * @param usernameTarget
	 * @param id
	 * @param tipo
	 * @return
	 */
	public boolean avalarUsuario(String username, String usernameTarget,
			ObjectId id, TipoAvalEnum tipo){
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Usuario usuarioTarget = usuarioRepository.findByUsernameIgnoreCase(usernameTarget);
		// Falta validar que no pueda avalar dos veces;
		
		final UsuarioAval usuarioAval = new UsuarioAval()
				.setTipo(tipo)
				.setUsuario(usuarioTarget);
		if(tipo.equals(TipoAvalEnum.CUALIDAD)){
			final Cualidad cualidad = cualidadRepository.findOne(id);
			usuarioAval.setCualidad(cualidad);
		}else{
			final Habilidad habilidad = habilidadRepository.findOne(id);
			usuarioAval.setHabilidad(habilidad);
		}
		
		usuario.getPersonasAvaladas().add(usuarioAval);
		
		final int pesoAval = usuario.getTipo().equals(TipoUsuariosEnum.PROFESOR)?2:1;
		
		usuarioTarget.getHabilidades()
			.stream()
			.filter(h -> h.getHabilidad().getId().equals(id))
			.forEach(h -> h.setCantidad(h.getCantidad()+pesoAval));
		
		return true;
	}

}
