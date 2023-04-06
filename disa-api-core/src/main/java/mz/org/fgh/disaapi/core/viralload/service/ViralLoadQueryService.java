/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface ViralLoadQueryService {

	int DEFAULT_PAGE_SIZE = 10;

	int MAX_PAGE_SIZE = 100;

	String DEFAULT_ORDER_BY = "createdAt";

	String DEFAULT_DIRECTION = "desc";

	String ASCENDING = "asc";

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findPendingByLocationCodeAndProvince(List<String> orgUnitCodes, String requestingProvinceName)
			throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findPendingByLocationCode(List<String> orgUnitCodes) throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	Page<ViralLoad> findByForm(ViralLoad example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link #findByForm(ViralLoad, List, LocalDateTime, LocalDateTime, Pageable)}
	 */
	@Deprecated
	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	mz.org.fgh.disaapi.core.viralload.model.Page<ViralLoad> findByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String ReferringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			int pageNumber,
			int pageSize,
			String orderBy,
			String direction) throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findAllByForm(ViralLoad example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link #findAllByForm(ViralLoad, List, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findAllByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	@PostFilter("principal.orgUnitCodes.contains(filterObject.healthFacilityLabCode)")
	List<ViralLoad> findViralLoadByNid(List<String> nids) throws BusinessException;

	@PostFilter("principal.orgUnitCodes.contains(filterObject.healthFacilityLabCode)")
	List<ViralLoad> findViralLoadByRequestId(List<String> requestIds) throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findByLocaationCodeAndStatus(List<String> orgUnitCodes, ViralLoadStatus viralLoadStatus)
			throws BusinessException;

	@PreAuthorize("principal.orgUnitCodes.containsAll(#orgUnitCodes)")
	List<ViralLoad> findByLocationCodeAndStatusBetweenDates(List<String> orgUnitCodes, ViralLoadStatus viralLoadStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;
}
