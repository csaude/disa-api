/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	List<ViralLoad> findPendingByLocationCodeAndProvince(List<String> locationCodes, String requestingProvinceName)
			throws BusinessException;

	List<ViralLoad> findPendingByLocationCode(List<String> locationCodes) throws BusinessException;

	Page<ViralLoad> findByForm(ViralLoad example, List<String> healthFacilityLabCodes, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) throws BusinessException;


	/**
	 * @deprecated see {@link #findByForm(ViralLoad, List, LocalDateTime, LocalDateTime, Pageable)}
	 */
	@Deprecated
	mz.org.fgh.disaapi.core.viralload.model.Page<ViralLoad> findByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
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

	List<ViralLoad> findAllByForm(ViralLoad example, List<String> healthFacilityLabCodes, LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	/**
	 * @deprecated see {@link #findAllByForm(ViralLoad, List, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	List<ViralLoad> findAllByForm(
			String requestId,
			String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate) throws BusinessException;

	List<ViralLoad> findViralLoadByNid(List<String> nids) throws BusinessException;

	List<ViralLoad> findViralLoadByRequestId(List<String> requestIds) throws BusinessException;

	List<ViralLoad> findByLocaationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus) throws BusinessException;

	List<ViralLoad> findByLocationCodeAndStatusBetweenDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;
}
