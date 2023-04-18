/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
public interface ViralLoadService {

	@PreAuthorize("principal.orgUnitCodes.contains(#viralLoad.healthFacilityLabCode)")
	ViralLoad updateViralLoad(ViralLoad viralLoad) throws BusinessException;

	/**
	 * Updates viral load given property values. For security only properties
	 * specified in {@link #getAllowedPropertiesForUpdate()} are allowed.
	 *
	 * @param context        The user context
	 * @param viralLoad      The viral load object to update
	 * @param propertyValues Viral load properties to update
	 * @return Updated viral load
	 * @throws BusinessException
	 */
	@PreAuthorize("principal.orgUnitCodes.contains(#viralLoad.healthFacilityLabCode)")
	ViralLoad updateViralLoad(ViralLoad viralLoad, Map<String, Object> propertyValues)
			throws BusinessException;

	/**
	 * @return A list of properties that can be updated using
	 *         {@link #updateViralLoad(ViralLoad, Map)}.
	 */
	List<String> getAllowedPropertiesForUpdate();

}
