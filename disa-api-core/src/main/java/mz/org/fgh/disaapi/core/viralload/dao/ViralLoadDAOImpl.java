/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import mz.co.msaude.boot.frameworks.dao.GenericDAOImpl;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Repository(ViralLoadDAOImpl.NAME)
public class ViralLoadDAOImpl extends GenericDAOImpl<ViralLoad, Long> implements ViralLoadDAO {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAOImpl";

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, String requestingProvinceName) throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("requestingProvinceName", requestingProvinceName).process());
	}
	
	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus) throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatusSimple,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).process());
	}
	
	@Override
	public List<ViralLoad> findByForm(String requestId, String nid, 
			String healthFacilityLabCode, String referringRequestID, 
			ViralLoadStatus viralLoadStatus, LocalDateTime startDate, LocalDateTime endDate, EntityStatus entityStatus) throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByForm,
				new ParamBuilder()
								  .add("entityStatus", entityStatus)
								  .add("requestId", requestId)
								  .add("nid", nid)
								  .add("healthFacilityLabCode", healthFacilityLabCode)
								  .add("referringRequestID", referringRequestID)
								  .add("viralLoadStatus", viralLoadStatus)
								  .add("startDate", startDate)
								  .add("endDate", endDate)
								  .process());
	}

	@Override
	public List<ViralLoad> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findViralLoadByNid,
				new ParamBuilder().add("nids", nids).add("entityStatus", entityStatus).process());
	}
	
	@Override
	public List<ViralLoad> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findViralLoadByRequestId,
			new ParamBuilder().add("requestIds", requestIds).add("entityStatus", entityStatus).process());
	}

	@Override
	public List<ViralLoad> findByStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).process());
	}

	@Override
	public List<ViralLoad> findByStatusAndDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {
		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByStatusAndDates,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("startDate", startDate).add("endDate", endDate)
						.process());
	}

	@Override
	public List<ViralLoad> findByLocationCodeStatusAndNotProcessingCause(List<String> locationCodes,
			ViralLoadStatus viralLoadStatus, EntityStatus entityStatus, NotProcessingCause notProcessingCause)
			throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeStatusAndNotProcessingCause,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("notProcessingCause", notProcessingCause)
						.process());
	}
}
