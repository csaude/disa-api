/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

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
import mz.co.msaude.boot.frameworks.util.ParamBuilder;
import mz.co.msaude.boot.frameworks.util.UuidFactory;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.Page;
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
	public Page<ViralLoad> findByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
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
		CriteriaQuery<ViralLoad> criteriaQuery = cb.createQuery(ViralLoad.class);
		Root<ViralLoad> vl = criteriaQuery.from(ViralLoad.class);

		vl.alias(alias);

		// Build query predicates
		Predicate restrictions = getSearchQueryRestrictions(
				requestId, nid, healthFacilityLabCode, referringRequestID,
				viralLoadStatus, notProcessingCause, startDate, endDate, search,
				entityStatus, cb, vl);

		// Build count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<ViralLoad> vlCount = countQuery.from(ViralLoad.class);
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
		TypedQuery<ViralLoad> q = entityManager.createQuery(criteriaQuery);
		q.setFirstResult((pageNumber - 1) * pageSize);
		q.setMaxResults(pageSize);

		return new Page<>(pageNumber, pageSize, count.longValue(), q.getResultList());
	}

	@Override
	public List<ViralLoad> findAllByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ViralLoad> criteriaQuery = cb.createQuery(ViralLoad.class);
		Root<ViralLoad> vl = criteriaQuery.from(ViralLoad.class);

		// Build query predicates
		Predicate restrictions = getSearchQueryRestrictions(
				requestId, nid, healthFacilityLabCode, referringRequestID,
				viralLoadStatus, notProcessingCause, startDate, endDate, null,
				entityStatus, cb, vl);

		criteriaQuery.select(vl);
		criteriaQuery.where(restrictions);

		setSearchQueryOrder(orderBy, direction, cb, criteriaQuery, vl);

		TypedQuery<ViralLoad> q = entityManager.createQuery(criteriaQuery);

		return q.getResultList();
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
	public ViralLoad create(final ViralLoad entity, String username) throws BusinessException {
		entity.setCreatedBy(username);
		entity.setCreatedAt(LocalDateTime.now());
		entity.active();

		if (entity.getUuid() == null) {
			entity.setUuid(UuidFactory.generate());
		}

		this.entityManager.persist(entity);

		return entity;
	}

	@Override
	public ViralLoad update(final ViralLoad entity, String username) throws BusinessException {
		entity.setUpdatedBy(username);
		entity.setUpdatedAt(LocalDateTime.now());

		this.entityManager.merge(entity);

		return entity;
	}

	private Predicate getSearchQueryRestrictions(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			EntityStatus entityStatus,
			CriteriaBuilder cb,
			Root<ViralLoad> vl) {

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

		if (viralLoadStatus != null) {
			predicates.add(cb.equal(vl.get("viralLoadStatus"), viralLoadStatus));
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
			CriteriaQuery<ViralLoad> criteriaQuery,
			Root<ViralLoad> vl) {
		if (direction.equalsIgnoreCase("asc")) {
			criteriaQuery.orderBy(cb.asc(vl.get(orderBy)));
		} else if (direction.equalsIgnoreCase("desc")) {
			criteriaQuery.orderBy(cb.desc(vl.get(orderBy)));
		}
	}
}
