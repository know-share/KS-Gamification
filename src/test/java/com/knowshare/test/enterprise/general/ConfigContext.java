/**
 * 
 */
package com.knowshare.test.enterprise.general;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowshare.enterprise.bean.avales.AvalBean;
import com.knowshare.enterprise.bean.avales.AvalFacade;
import com.knowshare.enterprise.bean.insignias.InsigniasBean;
import com.knowshare.enterprise.bean.insignias.InsigniasFacade;
import com.knowshare.enterprise.bean.leaderboard.LeaderBean;
import com.knowshare.enterprise.bean.leaderboard.LeaderFacade;
import com.knowshare.entities.academia.Carrera;
import com.knowshare.entities.ludificacion.Insignia;
import com.knowshare.entities.perfilusuario.Cualidad;
import com.knowshare.entities.perfilusuario.Gusto;
import com.knowshare.entities.perfilusuario.Habilidad;
import com.knowshare.entities.perfilusuario.Personalidad;
import com.knowshare.entities.perfilusuario.Usuario;
import com.mongodb.MongoClient;

/**
 * Configuración de contexto para las pruebas. Se cargan los bean de negocio que
 * serán necesarios para la ejecución de las pruebas
 * 
 * @author Miguel Montañez
 *
 */
@Lazy
@Configuration
@EnableMongoRepositories(basePackages = { "com.knowshare.enterprise.repository" })
@PropertySource("classpath:database.properties")
public class ConfigContext {
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	public void initData() throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		
		Insignia[] insignias = mapper.readValue(
				ResourceUtils.getURL("classpath:data/insignias.json").openStream(),Insignia[].class);
		Carrera[] carreras = mapper.readValue(
				ResourceUtils.getURL("classpath:data/carreras.json").openStream(),Carrera[].class);
		Habilidad[] habilidades = mapper.readValue(
				ResourceUtils.getURL("classpath:data/habilidades.json").openStream(),Habilidad[].class);
		Cualidad[] cualidades = mapper.readValue(
				ResourceUtils.getURL("classpath:data/cualidades.json").openStream(),Cualidad[].class);
		Gusto[] gustos = mapper.readValue(
				ResourceUtils.getURL("classpath:data/gustos.json").openStream(),Gusto[].class);
		Personalidad[] personalidades = mapper.readValue(
				ResourceUtils.getURL("classpath:data/personalidades.json").openStream(),Personalidad[].class);
		Usuario[] usuarios = mapper.readValue(
				ResourceUtils.getURL("classpath:data/usuarios.json").openStream(),Usuario[].class);
		
		this.mongoTemplate().insertAll(Arrays.asList(insignias));
		this.mongoTemplate().insertAll(Arrays.asList(carreras));
		this.mongoTemplate().insertAll(Arrays.asList(habilidades));
		this.mongoTemplate().insertAll(Arrays.asList(cualidades));
		this.mongoTemplate().insertAll(Arrays.asList(gustos));
		this.mongoTemplate().insertAll(Arrays.asList(personalidades));
		this.mongoTemplate().insertAll(Arrays.asList(usuarios));
		
		this.createIndexes();
		String command = "mongodump --host " +env.getProperty("db.host") + " --port " + env.getProperty("db.port")
	            + " -d " + env.getProperty("db.name") +" -o \"./target/\"";
		Runtime.getRuntime().exec(command);
	}
	
	@Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
        		new MongoClient(env.getProperty("db.host"),
        						Integer.parseInt(env.getProperty("db.port"))),
        			env.getProperty("db.name"));
    }
	
	@Bean
	public InsigniasFacade getInsigniasFacade(){
		return new InsigniasBean();
	}
	
	@Bean
	public AvalFacade getAvalFacade(){
		return new AvalBean();
	}
	
	@Bean
	public LeaderFacade getLeaderFacade(){
		return new LeaderBean();
	}
	
	@PreDestroy
	public void destroy() throws IOException{
		this.mongoTemplate().getDb().dropDatabase();
	}
	
	// ---------------------------
	// PRIVATE METHODS
	// ---------------------------
	
	private void createIndexes(){
		//db.habilidad.createIndex({'nombre':'text'},{ default_language: 'spanish' });
		TextIndexDefinition textIndex = TextIndexDefinition.builder()
				.onField("nombre")
				.withDefaultLanguage("spanish")
				.build();
		this.mongoTemplate().indexOps(Habilidad.class).ensureIndex(textIndex);
		
		//db.usuario.createIndex( { 'nombre': 'text','apellido':'text' },{ default_language: 'spanish' } );
		textIndex = TextIndexDefinition.builder()
				.onField("nombre")
				.onField("apellido")
				.withDefaultLanguage("spanish")
				.build();
		this.mongoTemplate().indexOps(Usuario.class).ensureIndex(textIndex);
	}
}
