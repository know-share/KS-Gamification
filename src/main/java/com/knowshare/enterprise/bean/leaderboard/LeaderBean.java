/**
 * 
 */
package com.knowshare.enterprise.bean.leaderboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import com.knowshare.dto.ludificacion.LeaderDTO;
import com.knowshare.enterprise.repository.academia.CarreraRepository;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoUsuariosEnum;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

/**
 * {@link LeaderFacade}
 * 
 * @author Felipe Bautista
 *
 */
@Component
public class LeaderBean implements LeaderFacade {

	@Autowired
	private CarreraRepository carreraRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final String CARRERAS_LIST = "carreras_list";
	private static final String MAX_AVAL = "maximo_aval";
	private static final String NOMBRE = "nombre";
	private static final String APELLIDO = "apellido";

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
				carrera.setId(c.getId());
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
	
	/**
	 * Consulta la cantidad de estudiantes que hay por
	 * carrera
	 * @return Una lista con campos especificando la carrera y
	 * la cantidad de estudiantes que hay
	 */
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
	
	/**
	 * Consulta la cantidad de avales que tienen los estudiantes
	 * de cierta carrera
	 * @param carrera
	 * @param tipo de usuario
	 * @return Una lista con campos especificando el usuario y su habilidad 
	 * con mayor aval.
	 */
	@SuppressWarnings("rawtypes")
	private List<Map> getUsuariosMap(String carrera,TipoUsuariosEnum tipo){
		final Aggregation agg = newAggregation(
				match(Criteria.where("carreras.$id").is(carrera)
						.and("tipo").is(tipo)),
				unwind("habilidades"),
				group("username")
					.first("habilidades.nombre").as("habilidad")
					.first(NOMBRE).as(NOMBRE)
					.first(APELLIDO).as(APELLIDO)
					.max("habilidades.cantidad").as(MAX_AVAL),
				sort(Direction.DESC,MAX_AVAL)
			);
		AggregationResults<Map> result = 
				mongoTemplate.aggregate(agg, Usuario.class, Map.class);
		return result.getMappedResults();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<LeaderDTO> usuariosLeader(String username, String carrera,TipoUsuariosEnum tipo) {
		final List<LeaderDTO> leader = new ArrayList<>();
		for(Map map: getUsuariosMap(carrera,tipo)){
			leader.add(new LeaderDTO()
					.setCantidad(Integer.parseInt(map.get(MAX_AVAL).toString()))
					.setUsername(map.get("_id").toString())
					.setNombre(map.get(NOMBRE).toString() +" "+ map.get(APELLIDO).toString())
					.setAval(map.get("habilidad").toString()));
		}
		return leader;
	}
}
