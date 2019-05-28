/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
public interface ViralLoadService {

	ViralLoad createViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException;

}
