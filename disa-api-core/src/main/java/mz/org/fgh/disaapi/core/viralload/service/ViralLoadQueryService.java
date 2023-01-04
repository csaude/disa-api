/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.List;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface ViralLoadQueryService {

	List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, String requestingProvinceName) throws BusinessException;

	List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes) throws BusinessException;

	List<ViralLoad> findByForm(String requestId, String nid,
			List<String> healthFacilityLabCode, String ReferringRequestID,
			ViralLoadStatus viralLoadStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;

	List<ViralLoad> findViralLoadByNid(List<String> nids) throws BusinessException;

	List<ViralLoad> findViralLoadByRequestId(List<String> requestIds) throws BusinessException;

	List<ViralLoad> findByStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus) throws BusinessException;

	List<ViralLoad> findByStatusAndDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;
}
