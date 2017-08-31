/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.repository.academia.CarreraRepository;
import com.knowshare.enterprise.repository.perfilusuario.UsuarioRepository;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.ludificacion.HabilidadAval;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoUsuariosEnum;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

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
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final String CARRERAS_LIST = "carreras_list";

	@SuppressWarnings("rawtypes")
	@Override
	public List<LeaderDTO> carrerasLeader() {
		final List<Carrera> carreras = carreraRepository.findAll();
		final List<Map> results = getCantidadEstudiantesXCarreras();
		List<LeaderDTO> carrerasxusuario = null;
		final Map<String, LeaderDTO> mapa = new HashMap<>();
		if (!carreras.isEmpty() ) {
			for (Carrera c : carreras) {
				LeaderDTO carrera = new LeaderDTO();
				carrera.setNombre(c.getNombre());
				carrera.setCantidad(0);
				mapa.put(c.getId(), carrera);
			}
			for(Map map : results){
				LeaderDTO carrera = mapa.get(map.get("_id"));
				if(null != carrera)
					carrera.setCantidad(Integer.parseInt(map.get("cantidad").toString()));
			}
		}
		carrerasxusuario = new ArrayList<>(mapa.values());
		Collections.sort(carrerasxusuario,(o1,o2)->o2.getCantidad()-o1.getCantidad()); 
		return carrerasxusuario;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Map> getCantidadEstudiantesXCarreras(){
		final Aggregation agg = newAggregation(
				unwind("enfasis"),
				group("id").addToSet("enfasis.carrera").as(CARRERAS_LIST),
				unwind(CARRERAS_LIST),
				group(CARRERAS_LIST).count().as("cantidad")
			);
		AggregationResults<Map> result = 
				mongoTemplate.aggregate(agg, Usuario.class, Map.class);
		return result.getMappedResults();
	}

	@Override
	public List<LeaderDTO> estudiantesLeader(String username, String carrera) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		boolean pertenece = false, usCarrera = false;
		List<LeaderDTO> usFinales = new ArrayList<LeaderDTO>();
		List<LeaderDTO> Final = new ArrayList<LeaderDTO>();
		String habilidad = "";
		int cantidad = -1;
		LeaderDTO nuevo = null; // toca ver que pasa con POS!
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
			if (i < 5 && usFinales.get(i).getNombre().equalsIgnoreCase(username)) {
				usCarrera = false;
			}
			if (i < 5 && !usCarrera) {
				Final.add(usFinales.get(i));
				if (i == 4)
					return Final;
			}
			if (i < 4 && usCarrera) {
				Final.add(usFinales.get(i));
			}
			if (i > 4 && usCarrera && usFinales.get(i).getNombre().equalsIgnoreCase(username)) {
				Final.add(usFinales.get(i));
				return Final;
			}
		}
		return Final;
	}

}
