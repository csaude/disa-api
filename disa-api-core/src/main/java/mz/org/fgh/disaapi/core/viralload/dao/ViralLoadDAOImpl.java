/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import mz.co.msaude.boot.frameworks.dao.GenericDAOImpl;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
@Repository(ViralLoadDAOImpl.NAME)
public class ViralLoadDAOImpl extends GenericDAOImpl<ViralLoad, Long> implements ViralLoadDAO {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAOImpl";

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus) throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).process());
	}

	@Override
	public List<ViralLoad> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findViralLoadByNid,
				new ParamBuilder().add("nids", nids).add("entityStatus", entityStatus).process());
	}
}
