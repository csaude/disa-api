/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.service.AbstractService;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
@Service(ViralLoadServiceImpl.NAME)
public class ViralLoadServiceImpl extends AbstractService implements ViralLoadService {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.service.ViralLoadServiceImpl";

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Override
	public ViralLoad createViralLoad(final UserContext context, final ViralLoad viralLoad) throws BusinessException {
		this.viralLoadDAO.create(context, viralLoad);
		return viralLoad;
	}

	@Override
	public ViralLoad updateViralLoad(UserContext context, ViralLoad viralLoad) throws BusinessException {
		this.viralLoadDAO.update(context, viralLoad);
		return viralLoad;
	}
}
