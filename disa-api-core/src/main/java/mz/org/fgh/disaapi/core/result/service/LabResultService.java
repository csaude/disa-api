/**
 *
 */
package mz.org.fgh.disaapi.core.result.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.result.model.LabResult;

/**
 * @author Stélio Moiane
 *
 */
public interface LabResultService {

	@PreAuthorize("principal.orgUnitCodes.contains(#labResult.healthFacilityLabCode)")
	LabResult updateLabResult(LabResult labResult) throws BusinessException;

	/**
	 * Updates viral load given property values. For security only properties
	 * specified in {@link #getAllowedPropertiesForUpdate()} are allowed.
	 *
	 * @param context        The user context
	 * @param labResult      The viral load object to update
	 * @param propertyValues Viral load properties to update
	 * @return Updated viral load
	 * @throws BusinessException
	 */
	@PreAuthorize("principal.orgUnitCodes.contains(#labResult.healthFacilityLabCode)")
	LabResult updateLabResult(LabResult labResult, Map<String, Object> propertyValues)
			throws BusinessException;

	/**
	 * @return A list of properties that can be updated using
	 *         {@link #updateLabResult(LabResult, Map)}.
	 */
	List<String> getAllowedPropertiesForUpdate();
	
	@PreAuthorize("principal.username=='aphl'") 
	List<LabResult> saveLabResult(List<LabResult> labResultList); 
}
