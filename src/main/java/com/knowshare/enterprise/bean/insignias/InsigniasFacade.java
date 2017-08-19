/**
 * 
 */
package com.knowshare.enterprise.bean.insignias;

import com.knowshare.dto.idea.IdeaDTO;

/**
 * @author Miguel Montañez
 *
 */
public interface InsigniasFacade {
	
	void insigniasCreacionIdea(IdeaDTO idea);

	void insigniasSeguir(String username);
	
	void insigniasAccionSolicitud(String username);
	
	void insigniasCompartir(IdeaDTO idea);
	
	void insigniasAgregarTGDirigido(String username);
	
	void insigniasAgregarFormacionAcademica(String username);
}
