/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.repository.academia.CarreraRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.ludificacion.HabilidadAval;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoUsuariosEnum;

/**
 * {@link LeaderFacade}
 * 
 * @author Felipe Bautista
 *
 */
@Component
public class LeaderBean implements LeaderFacade {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CarreraRepository carreraRepository;

	@Override
	public List<LeaderDTO> carrerasLeader() {
		List<Carrera> carreras = carreraRepository.findAll();
		List<Usuario> usuarios = usuarioRepository.findAll();
		List<LeaderDTO> carrerasxusuario = null;
		Map<String, LeaderDTO> mapa = new HashMap<String, LeaderDTO>();
		if (!carreras.isEmpty() && !usuarios.isEmpty()) {
			for (Carrera c : carreras) {
				LeaderDTO carrera = new LeaderDTO();
				carrera.setNombre(c.getNombre());
				carrera.setCantidad(0);
				mapa.put(c.getId(), carrera);
			}
			for (Usuario u : usuarios) {
				if (u.getCarreras() != null) {
					for (Carrera cu : u.getCarreras()) {
						LeaderDTO dto = mapa.get(cu.getId());
						if (dto != null) {
							dto.setCantidad(dto.getCantidad() + 1);
							mapa.put(cu.getId(), dto);
						}
					}
				}
			}
		}
		carrerasxusuario = new ArrayList<LeaderDTO>(mapa.values());
		Collections.sort(carrerasxusuario, new Comparator<LeaderDTO>() {
			@Override
			public int compare(final LeaderDTO object1, final LeaderDTO object2) {
				return object2.getCantidad() - object1.getCantidad();
			}
		});
		return carrerasxusuario;
	}

	@Override
	public List<LeaderDTO> estudiantesLeader(String username, String carrera) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		boolean pertenece = false, usCarrera = false;
		List<LeaderDTO> usFinales = new ArrayList<LeaderDTO>();
		List<LeaderDTO> Final = new ArrayList<LeaderDTO>();
		String habilidad = "";
		int cantidad = -1;
		LeaderDTO nuevo = null;
		for (Usuario us : usuarios) {
			if (us.getTipo() == TipoUsuariosEnum.ESTUDIANTE) {
				for (Carrera ca : us.getCarreras()) {
					if (ca.getNombre().equalsIgnoreCase(carrera)) {
						pertenece = true;
						if (us.getNombre().equalsIgnoreCase(username))
							usCarrera = true;
					}
				}
				if (pertenece) {
					for (HabilidadAval ha : us.getHabilidades()) {
						if (ha.getCantidad() > cantidad) {
							habilidad = ha.getHabilidad().getNombre();
							cantidad = ha.getCantidad();
						}
					}

					nuevo = new LeaderDTO();
					nuevo.setAval(habilidad);
					nuevo.setCantidad(cantidad);
					nuevo.setNombre(us.getNombre());
					usFinales.add(nuevo);

					cantidad = -1;
				}
				pertenece = false;
			}
		}
		Collections.sort(usFinales, new Comparator<LeaderDTO>() {
			@Override
			public int compare(final LeaderDTO object1, final LeaderDTO object2) {
				return object2.getCantidad() - object1.getCantidad();
			}
		});
		for (int i = 0; i < usFinales.size(); i++) {
			if (i < 10 && usFinales.get(i).getNombre().equalsIgnoreCase(username)) {
				usCarrera = false;
			}
			if (i < 10 && !usCarrera) {
				Final.add(usFinales.get(i));
				if (i == 9)
					return Final;
			}
			if (i < 9 && usCarrera) {
				Final.add(usFinales.get(i));
			}
			if (i > 9 && usCarrera && usFinales.get(i).getNombre().equalsIgnoreCase(username)) {
				Final.add(usFinales.get(i));
				return Final;
			}
		}
		return Final;
	}

}
