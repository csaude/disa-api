/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Repository(ViralLoadDAOImpl.NAME)
public class ViralLoadDAOImpl implements ViralLoadDAO {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAOImpl";

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, String requestingProvinceName) throws BusinessException {

		return this.findByNamedQuery(ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("viralLoadStatus", viralLoadStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("requestingProvinceName", requestingProvinceName)
						.process());
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
			final List<String> healthFacilityLabCode, String referringRequestID, 
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
	public List<ViralLoad> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus)
			throws BusinessException {
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

	private List<ViralLoad> findByNamedQuery(final String queryName, final Map<String, ? extends Object> params)
			throws BusinessException {

		final TypedQuery<ViralLoad> query = this.entityManager.createNamedQuery(queryName, ViralLoad.class);

		for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		return query.getResultList();
	}

	@Override
	public ViralLoad create(final UserContext context, final ViralLoad entity) throws BusinessException {
		entity.setCreatedBy(context.getUuid());
		entity.setCreatedAt(LocalDateTime.now());
		entity.active();

		if (entity.getUuid() == null) {
			entity.setUuid(UuidFactory.generate());
		}

		this.entityManager.persist(entity);

		return entity;
	}

	@Override
	public ViralLoad update(final UserContext context, final ViralLoad entity) throws BusinessException {
		entity.setUpdatedBy(context.getUuid());
		entity.setUpdatedAt(LocalDateTime.now());

		this.entityManager.merge(entity);

		return entity;
	}
}
