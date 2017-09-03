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
	
	public boolean avalarUsuario(String username, String usernameTarget,
			ObjectId id, TipoAvalEnum tipo){
		final Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username);
		final Usuario usuarioTarget = usuarioRepository.findByUsernameIgnoreCase(usernameTarget);
		
		final int pesoAval = usuario.getTipo().equals(TipoUsuariosEnum.PROFESOR)?2:1;
		
		if(!existAval(usuario, usuarioTarget, id,tipo)){		
			final UsuarioAval usuarioAval = new UsuarioAval()
					.setTipo(tipo)
					.setUsuario(usuarioTarget);
			if(tipo.equals(TipoAvalEnum.CUALIDAD)){
				final Cualidad cualidad = cualidadRepository.findOne(id);
				usuarioAval.setCualidad(cualidad);
				usuarioTarget.getCualidadesProfesor()
					.stream()
					.filter(c -> c.getCualidad().getId().equals(id))
					.forEach(c -> c.setCantidad(c.getCantidad()+pesoAval));
			}else{
				final Habilidad habilidad = habilidadRepository.findOne(id);
				usuarioAval.setHabilidad(habilidad);
				usuarioTarget.getHabilidades()
					.stream()
					.filter(h -> h.getHabilidad().getId().equals(id))
					.forEach(h -> h.setCantidad(h.getCantidad()+pesoAval));
			}
			
			usuario.getPersonasAvaladas().add(usuarioAval);
			
			return usuarioRepository.save(usuario) != null && null != usuarioRepository.save(usuarioTarget);
		}
		
		return false;
	}
	
	private boolean existAval(Usuario usuario, Usuario usuarioTarget, ObjectId id,
			TipoAvalEnum tipo){
		if(tipo.equals(TipoAvalEnum.CUALIDAD))
			return usuario.getPersonasAvaladas()
					.stream()
					.filter(pa -> pa.getCualidad() != null && pa.getCualidad().getId().equals(id) && 
							pa.getUsuario().getId().equals(usuarioTarget.getId()))
					.count() > 0;
		return usuario.getPersonasAvaladas()
				.stream()
				.filter(pa -> pa.getHabilidad() != null && pa.getHabilidad().getId().equals(id) && 
						pa.getUsuario().getId().equals(usuarioTarget.getId()))
				.count() > 0;
	}

}
