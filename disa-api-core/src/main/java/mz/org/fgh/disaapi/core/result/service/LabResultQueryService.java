/**
 *
 */
package mz.org.fgh.disaapi.core.result.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface LabResultQueryService {

	int DEFAULT_PAGE_SIZE = 10;

	int MAX_PAGE_SIZE = 100;

	String DEFAULT_ORDER_BY = "createdAt";

	String DEFAULT_DIRECTION = "desc";

	String ASCENDING = "asc";

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findPendingByLocationCodeAndProvince(List<String> orgUnitCodes, String requestingProvinceName)
			throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findPendingByLocationCode(List<String> orgUnitCodes) throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	Page<LabResult> findByForm(LabResult example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link #findByForm(LabResult, List, LocalDateTime, LocalDateTime, Pageable)}
	 */
	@Deprecated
	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	mz.org.fgh.disaapi.core.result.model.Page<LabResult> findByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String ReferringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			TypeOfResult typeOfResult,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			int pageNumber,
			int pageSize,
			String orderBy,
			String direction) throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findAllByForm(LabResult example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link #findAllByForm(LabResult, List, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findAllByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			TypeOfResult typeOfResult,
			LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	@PostFilter("principal.orgUnitCodes.contains(filterObject.healthFacilityLabCode)")
	List<LabResult> findViralLoadByNid(List<String> nids) throws BusinessException;

	@Deprecated
	@PostFilter("principal.orgUnitCodes.contains(filterObject.healthFacilityLabCode)")
	List<LabResult> findViralLoadByRequestId(List<String> requestIds) throws BusinessException;

	/**
	 * @param id
	 * @return A List containing a single result. For now a List is used instead of
	 *         the actual LabResult so it is possible to use @PostFilter.
	 */
	@PostFilter("principal.orgUnitCodes.contains(filterObject.healthFacilityLabCode)")
	List<LabResult> findById(Long id) throws NotFoundBusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findByLocaationCodeAndStatus(List<String> orgUnitCodes, LabResultStatus labResultStatus)
			throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<LabResult> findByLocationCodeAndStatusBetweenDates(List<String> orgUnitCodes, LabResultStatus labResultStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;
}
