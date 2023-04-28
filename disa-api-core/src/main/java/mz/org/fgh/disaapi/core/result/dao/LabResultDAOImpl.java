/**
 *
 */
package mz.org.fgh.disaapi.core.result.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.Page;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Repository(LabResultDAOImpl.NAME)
public class LabResultDAOImpl implements LabResultDAO {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAOImpl";

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<LabResult> findByLocationCodeAndStatus(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus, String requestingProvinceName) throws BusinessException {

		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("labResultStatus", labResultStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("requestingProvinceName", requestingProvinceName)
						.process());
	}

	@Override
	public List<LabResult> findByLocationCodeAndStatus(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus) throws BusinessException {

		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByLocationCodeAndStatusSimple,
				new ParamBuilder().add("labResultStatus", labResultStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).process());
	}

	@Override
	public Page<LabResult> findByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			int pageNumber,
			int pageSize,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException {

		String alias = "vl";

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LabResult> criteriaQuery = cb.createQuery(LabResult.class);
		Root<LabResult> vl = criteriaQuery.from(LabResult.class);

		vl.alias(alias);

		// Build query predicates
		Predicate restrictions = getSearchQueryRestrictions(
				requestId, nid, healthFacilityLabCode, referringRequestID,
				labResultStatus, notProcessingCause, startDate, endDate, search,
				entityStatus, cb, vl);

		// Build count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<LabResult> vlCount = countQuery.from(LabResult.class);
		// Use same alias so restrictions match
		vlCount.alias(alias);
		countQuery.select(cb.count(vlCount));
		countQuery.where(restrictions);
		Long count = entityManager.createQuery(countQuery).getSingleResult();

		criteriaQuery.select(vl);
		criteriaQuery.where(restrictions);

		// Sorting
		setSearchQueryOrder(orderBy, direction, cb, criteriaQuery, vl);

		// Get paginated results
		TypedQuery<LabResult> q = entityManager.createQuery(criteriaQuery);
		q.setFirstResult((pageNumber - 1) * pageSize);
		q.setMaxResults(pageSize);

		return new Page<>(pageNumber, pageSize, count.longValue(), q.getResultList());
	}

	@Override
	public List<LabResult> findAllByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LabResult> criteriaQuery = cb.createQuery(LabResult.class);
		Root<LabResult> vl = criteriaQuery.from(LabResult.class);

		// Build query predicates
		Predicate restrictions = getSearchQueryRestrictions(
				requestId, nid, healthFacilityLabCode, referringRequestID,
				labResultStatus, notProcessingCause, startDate, endDate, null,
				entityStatus, cb, vl);

		criteriaQuery.select(vl);
		criteriaQuery.where(restrictions);

		setSearchQueryOrder(orderBy, direction, cb, criteriaQuery, vl);

		TypedQuery<LabResult> q = entityManager.createQuery(criteriaQuery);

		return q.getResultList();
	}

	@Override
	public List<LabResult> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByNid,
				new ParamBuilder().add("nids", nids).add("entityStatus", entityStatus).process());
	}

	@Override
	public List<LabResult> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus)
			throws BusinessException {
		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByRequestId,
				new ParamBuilder().add("requestIds", requestIds).add("entityStatus", entityStatus).process());
	}

	@Override
	public List<LabResult> findByStatus(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus) throws BusinessException {
		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByLocationCodeAndStatus,
				new ParamBuilder().add("labResultStatus", labResultStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).process());
	}

	@Override
	public List<LabResult> findByStatusAndDates(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {
		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByStatusAndDates,
				new ParamBuilder().add("labResultStatus", labResultStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("startDate", startDate).add("endDate", endDate)
						.process());
	}

	@Override
	public List<LabResult> findByLocationCodeStatusAndNotProcessingCause(List<String> locationCodes,
			LabResultStatus labResultStatus, EntityStatus entityStatus, NotProcessingCause notProcessingCause)
			throws BusinessException {

		return this.findByNamedQuery(LabResultDAO.QUERY_NAME.findByLocationCodeStatusAndNotProcessingCause,
				new ParamBuilder().add("labResultStatus", labResultStatus).add("entityStatus", entityStatus)
						.add("locationCodes", locationCodes).add("notProcessingCause", notProcessingCause)
						.process());
	}

	private List<LabResult> findByNamedQuery(final String queryName, final Map<String, ? extends Object> params)
			throws BusinessException {

		final TypedQuery<LabResult> query = this.entityManager.createNamedQuery(queryName, LabResult.class);

		for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}

		return query.getResultList();
	}

	@Override
	public LabResult create(final UserContext context, final LabResult entity) throws BusinessException {
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
	public LabResult update(final UserContext context, final LabResult entity) throws BusinessException {
		entity.setUpdatedBy(context.getUuid());
		entity.setUpdatedAt(LocalDateTime.now());

		this.entityManager.merge(entity);

		return entity;
	}

	private Predicate getSearchQueryRestrictions(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			EntityStatus entityStatus,
			CriteriaBuilder cb,
			Root<LabResult> vl) {

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(vl.get("entityStatus"), entityStatus));
		predicates.add(vl.get("healthFacilityLabCode").in(healthFacilityLabCode));

		if (startDate != null) {
			predicates.add(cb.greaterThan(vl.get("createdAt"), startDate));
		}

		if (endDate != null) {
			predicates.add(cb.lessThan(vl.get("createdAt"), endDate));
		}

		if (!StringUtils.isEmpty(requestId)) {
			predicates.add(cb.equal(vl.get("requestId"), requestId));
		}

		if (!StringUtils.isEmpty(referringRequestID)) {
			predicates.add(cb.equal(vl.get("referringRequestID"), referringRequestID));
		}

		if (labResultStatus != null) {
			predicates.add(cb.equal(vl.get("labResultStatus"), labResultStatus));
		}

		if (notProcessingCause != null) {
			predicates.add(cb.equal(vl.get("notProcessingCause"), notProcessingCause));
		}

		if (!StringUtils.isEmpty(nid)) {
			predicates.add(cb.equal(vl.get("nid"), nid));
		}

		if (!StringUtils.isEmpty(search)) {
			predicates.add(cb.or(
					cb.like(vl.get("nid"), search + "%"),
					cb.like(vl.get("firstName"), search + "%"),
					cb.like(vl.get("lastName"), search + "%"),
					cb.like(vl.get("requestId"), search + "%")));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}

	private void setSearchQueryOrder(String orderBy, String direction, CriteriaBuilder cb,
			CriteriaQuery<LabResult> criteriaQuery,
			Root<LabResult> vl) {
		if (direction.equalsIgnoreCase("asc")) {
			criteriaQuery.orderBy(cb.asc(vl.get(orderBy)));
		} else if (direction.equalsIgnoreCase("desc")) {
			criteriaQuery.orderBy(cb.desc(vl.get(orderBy)));
		}
	}
}
