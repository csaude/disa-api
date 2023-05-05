/**
 *
 */
package mz.org.fgh.disaapi.core.result.dao;

import java.time.LocalDateTime;
import java.util.List;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.Page;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface LabResultDAO {
	class QUERY {
		public static final String findByLocationCodeAndStatus = "SELECT lr FROM LabResult lr WHERE lr.healthFacilityLabCode IN (:locationCodes)and lr.labResultStatus = :labResultStatus AND lr.entityStatus = :entityStatus "
				+ "AND lr.requestingProvinceName = :requestingProvinceName";
		public static final String findByLocationCodeAndStatusSimple = "SELECT lr FROM LabResult lr WHERE lr.healthFacilityLabCode IN (:locationCodes) and lr.labResultStatus = :labResultStatus AND lr.entityStatus = :entityStatus";
		public static final String findByStatusAndDates = "SELECT lr FROM LabResult lr WHERE lr.healthFacilityLabCode IN (:locationCodes)and lr.labResultStatus = :labResultStatus AND lr.entityStatus = :entityStatus and lr.createdAt between :startDate and :endDate";
		public static final String findByNid = "SELECT lr FROM LabResult lr WHERE lr.nid IN (:nids) AND lr.entityStatus = :entityStatus";
		public static final String findByRequestId = "SELECT lr FROM LabResult lr WHERE lr.requestId IN (:requestIds) AND lr.entityStatus = :entityStatus";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "SELECT lr FROM LabResult lr WHERE lr.healthFacilityLabCode IN (:locationCodes)and lr.labResultStatus = :labResultStatus AND lr.entityStatus = :entityStatus and lr.notProcessingCause = :notProcessingCause";

	}

	class QUERY_NAME {
		public static final String findByLocationCodeAndStatus = "ViralLoad.findByLocationCodeAndStatus";
		public static final String findByLocationCodeAndStatusSimple = "ViralLoad.findByLocationCodeAndStatusSimple";
		public static final String findByStatusAndDates = "ViralLoad.findByStatusAndDates";
		public static final String findByNid = "ViralLoad.findByNid";
		public static final String findByRequestId = "ViralLoad.findByRequestId";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "ViralLoad.findByLocationCodeStatusAndNotProcessingCause";

	}

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByLocationCodeAndStatus(List, LabResultStatus, EntityStatus, String)}
	 */
	@Deprecated
	List<LabResult> findByLocationCodeAndStatus(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus, String requestingProvinceName) throws BusinessException;

	/**
	 *
	 * @deprecated see
	 *             {@link LabResultRepository#findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(List, LabResultStatus, EntityStatus)}
	 */
	@Deprecated
	List<LabResult> findByLocationCodeAndStatus(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultQueryService#findByForm(LabResult, List, LocalDateTime, LocalDateTime, org.springframework.data.domain.Pageable)}
	 */
	@Deprecated
	Page<LabResult> findByForm(
			String requestId, String nid,
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
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see {@link LabResultQueryService#findAllByForm(LabResult, List, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	List<LabResult> findAllByForm(
			String requestId, String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(List, LabResultStatus, EntityStatus)}
	 */
	@Deprecated
	List<LabResult> findByStatus(List<String> locationCodes, LabResultStatus labResultStatus, EntityStatus entityStatus)
			throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByStatusAndDates(List, LabResultStatus, EntityStatus, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	List<LabResult> findByStatusAndDates(List<String> locationCodes, LabResultStatus labResultStatus,
			EntityStatus entityStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByNidAndEntityStatus(List, EntityStatus)}
	 */
	@Deprecated
	List<LabResult> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByRequestIdAndEntityStatus(List, EntityStatus)}
	 */
	@Deprecated
	List<LabResult> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus)
			throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link LabResultRepository#findByLocationCodeStatusAndNotProcessingCause(List, LabResultStatus, EntityStatus, NotProcessingCause)}
	 */
	@Deprecated
	List<LabResult> findByLocationCodeStatusAndNotProcessingCause(List<String> locationCodes,
			LabResultStatus labResultStatus, EntityStatus entityStatus, NotProcessingCause reasonForNotProcessing)
			throws BusinessException;

	LabResult create(final UserContext context, final LabResult entity) throws BusinessException;

	LabResult update(final UserContext context, final LabResult entity) throws BusinessException;
}
