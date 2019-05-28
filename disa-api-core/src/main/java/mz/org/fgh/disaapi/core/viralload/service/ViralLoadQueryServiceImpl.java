/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
@Service(ViralLoadQueryServiceImpl.NAME)
public class ViralLoadQueryServiceImpl implements ViralLoadQueryService {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryServiceImpl";

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Override
	public List<ViralLoad> findAllViralLoad() throws BusinessException {
		return this.viralLoadDAO.findAll();
	}
}
