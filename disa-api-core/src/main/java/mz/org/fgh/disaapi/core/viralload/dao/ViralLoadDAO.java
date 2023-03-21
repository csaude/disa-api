/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.time.LocalDateTime;
import java.util.List;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.co.msaude.boot.frameworks.model.UserContext;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.Page;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.repository.ViralLoadRepository;
import mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryService;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface ViralLoadDAO {
	class QUERY {
		public static final String findByLocationCodeAndStatus = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus "
				+ "AND vl.requestingProvinceName = :requestingProvinceName";
		public static final String findByLocationCodeAndStatusSimple = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus";
		public static final String findByStatusAndDates = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.createdAt between :startDate and :endDate";
		public static final String findViralLoadByNid = "SELECT vl FROM ViralLoad vl WHERE vl.nid IN (:nids) AND vl.entityStatus = :entityStatus";
		public static final String findViralLoadByRequestId = "SELECT vl FROM ViralLoad vl WHERE vl.requestId IN (:requestIds) AND vl.entityStatus = :entityStatus";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.notProcessingCause = :notProcessingCause";

	}

	class QUERY_NAME {
		public static final String findByLocationCodeAndStatus = "ViralLoad.findByLocationCodeAndStatus";
		public static final String findByLocationCodeAndStatusSimple = "ViralLoad.findByLocationCodeAndStatusSimple";
		public static final String findByStatusAndDates = "ViralLoad.findByStatusAndDates";
		public static final String findViralLoadByNid = "ViralLoad.findViralLoadByNid";
		public static final String findViralLoadByRequestId = "ViralLoad.findViralLoadByRequestId";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "ViralLoad.findByLocationCodeStatusAndNotProcessingCause";

	}

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByLocationCodeAndStatus(List, ViralLoadStatus, EntityStatus, String)}
	 */
	@Deprecated
	List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, String requestingProvinceName) throws BusinessException;

	/**
	 *
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByHealthFacilityLabCodeInAndViralLoadStatusAndEntityStatus(List, ViralLoadStatus, EntityStatus)}
	 */
	@Deprecated
	List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadQueryService#findByForm(ViralLoad, List, LocalDateTime, LocalDateTime, org.springframework.data.domain.Pageable)}
	 */
	@Deprecated
	Page<ViralLoad> findByForm(
			String requestId, String nid,
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
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see {@link ViralLoadQueryService#findAllByForm(ViralLoad, List, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	List<ViralLoad> findAllByForm(
			String requestId, String nid,
			List<String> healthFacilityLabCode,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByHealthFacilityLabCodeAndViralLoadStatusAndEntityStatus(List, ViralLoadStatus, EntityStatus)}
	 */
	@Deprecated
	List<ViralLoad> findByStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus, EntityStatus entityStatus)
			throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByStatusAndDates(List, ViralLoadStatus, EntityStatus, LocalDateTime, LocalDateTime)}
	 */
	@Deprecated
	List<ViralLoad> findByStatusAndDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByNidAndEntityStatus(List, EntityStatus)}
	 */
	@Deprecated
	List<ViralLoad> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByRequestIdAndEntityStatus(List, EntityStatus)}
	 */
	@Deprecated
	List<ViralLoad> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus)
			throws BusinessException;

	/**
	 * @deprecated see
	 *             {@link ViralLoadRepository#findByLocationCodeStatusAndNotProcessingCause(List, ViralLoadStatus, EntityStatus, NotProcessingCause)}
	 */
	@Deprecated
	List<ViralLoad> findByLocationCodeStatusAndNotProcessingCause(List<String> locationCodes,
			ViralLoadStatus viralLoadStatus, EntityStatus entityStatus, NotProcessingCause reasonForNotProcessing)
			throws BusinessException;

	ViralLoad create(final UserContext context, final ViralLoad entity) throws BusinessException;

	ViralLoad update(final UserContext context, final ViralLoad entity) throws BusinessException;
}
