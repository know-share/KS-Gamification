/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.knowshare.dto.ludificacion.CarreraLeaderDTO;
import com.knowshare.enterprise.repository.academia.CarreraRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.perfilusuario.Usuario;

/**
 * {@link LeaderFacade}
 * @author Felipe Bautista
 *
 */
@Component
public class LeaderBean implements LeaderFacade{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CarreraRepository carreraRepository;
	
	@Override
	public List<CarreraLeaderDTO> carrerasLeader() {
		List<Carrera>carreras = carreraRepository.findAll();
		List<Usuario> usuarios = usuarioRepository.findAll();
		List<CarreraLeaderDTO> carrerasxusuario = new ArrayList<CarreraLeaderDTO>();
		if(!carreras.isEmpty() && !usuarios.isEmpty()) {
			for(Carrera c: carreras) {
				CarreraLeaderDTO carrera = new CarreraLeaderDTO();
				carrera.setCarrera(c.getNombre());
				carrera.setCantidad(0);
				carrerasxusuario.add(carrera);
			}	
			for(Usuario u: usuarios) {
				if(u.getCarreras()!=null) {
					for(Carrera cu : u.getCarreras()) {
						int index = findCarrera(cu.getNombre(), carrerasxusuario); 
						if(index!=-1) {
							carrerasxusuario.get(index).setCantidad(carrerasxusuario.get(index).getCantidad()+1);
						}
					}
				}
			}
		} // hacer map -- para usuarios mostrar unos luego puntos... y posicion de uno, la idea es por carrera y mostrar 
		// la que mas avales tenga
		Collections.sort(carrerasxusuario, new Comparator<CarreraLeaderDTO>() {
		      @Override
		      public int compare(final CarreraLeaderDTO object1, final CarreraLeaderDTO object2) {
		          return object2.getCantidad()-object1.getCantidad();
		      }
		  });
		return carrerasxusuario;
	}
	
	private int findCarrera (String nombre,List<CarreraLeaderDTO> carrerasxusuario ) {
		int cont = 0;
		if(!carrerasxusuario.isEmpty()) {
			for(CarreraLeaderDTO c: carrerasxusuario) {
				if(nombre.equals(c.getCarrera())) {
					return cont;
				}
				cont++;
			}
		}
		return -1;
	}
}
