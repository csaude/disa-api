/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.util.Map;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
public interface ViralLoadService {

	ViralLoad createViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException;

	ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException;

	/**
	 * Updates viral load given property values.
	 *
	 * @param context The user context
	 * @param viralLoad The viral load object to update
	 * @param propertyValues Viral load properties to update
	 * @return Updated viral load
	 * @throws BusinessException
	 */
	ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad, Map<String, Object> propertyValues) throws BusinessException;

}
