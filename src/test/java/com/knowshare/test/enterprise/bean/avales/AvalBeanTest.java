/**
 * 
 */
package com.knowshare.test.enterprise.bean.avales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.knowshare.enterprise.bean.avales.AvalFacade;
import com.knowshare.entities.perfilusuario.Cualidad;
import com.knowshare.entities.perfilusuario.Habilidad;
import com.knowshare.entities.perfilusuario.Usuario;
import com.knowshare.enums.TipoAvalEnum;
import com.knowshare.test.enterprise.general.AbstractTest;

/**
 * @author Miguel Montañez
 *
 */
public class AvalBeanTest extends AbstractTest{
	
	@Autowired
	private AvalFacade avalBean;
	
	private Habilidad habilidad;
	private Cualidad cualidad;
	
	@Before
	public void init(){
		habilidad = mongoTemplate.findOne(new Query(Criteria.where("nombre")
				.is("Habilidad Profesional sistemas 1")), Habilidad.class);
		cualidad = mongoTemplate.findOne(new Query(Criteria.where("nombre")
				.is("Cualidad 1")), Cualidad.class);
	}

	@Test
	public void avalarUsuarioTest(){
		boolean result = avalBean.avalarUsuario("minmiguelm", "pablo.gaitan", cualidad.getId(), TipoAvalEnum.CUALIDAD);
		Usuario usuario = mongoTemplate.findOne(new Query(Criteria.where("username").is("pablo.gaitan")), Usuario.class);
		assertTrue(result);
		assertEquals(Integer.valueOf(1), usuario.getCualidadesProfesor().get(0).getCantidad());
		usuario = mongoTemplate.findOne(new Query(Criteria.where("username").is("MinMiguelM")), Usuario.class);
		assertEquals(1, usuario.getPersonasAvaladas().size());
		
		result = avalBean.avalarUsuario("minmiguelm", "pablo.gaitan", cualidad.getId(), TipoAvalEnum.CUALIDAD);
		assertFalse(result);
		usuario = mongoTemplate.findOne(new Query(Criteria.where("username").is("MinMiguelM")), Usuario.class);
		assertEquals(1, usuario.getPersonasAvaladas().size());
		
		result = avalBean.avalarUsuario("pablo.gaitan","minmiguelm", habilidad.getId(), TipoAvalEnum.HABILIDAD);
		assertTrue(result);
		usuario = mongoTemplate.findOne(new Query(Criteria.where("username").is("MinMiguelM")), Usuario.class);
		assertEquals(Integer.valueOf(2), usuario.getHabilidades().get(1).getCantidad());
		
		usuario = mongoTemplate.findOne(new Query(Criteria.where("username").is("pablo.gaitan")), Usuario.class);
		assertEquals(1, usuario.getPersonasAvaladas().size());
	}
}
