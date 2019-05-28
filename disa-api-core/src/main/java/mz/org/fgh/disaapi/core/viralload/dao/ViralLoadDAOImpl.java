/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import mz.co.msaude.boot.frameworks.dao.GenericDAOImpl;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;

/**
 * @author Stélio Moiane
 *
 */
@Repository(ViralLoadDAOImpl.NAME)
public class ViralLoadDAOImpl extends GenericDAOImpl<ViralLoad, Long> implements ViralLoadDAO {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAOImpl";

	@Override
	public List<ViralLoad> findAll() throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findAll, new ParamBuilder().process());
	}
}
