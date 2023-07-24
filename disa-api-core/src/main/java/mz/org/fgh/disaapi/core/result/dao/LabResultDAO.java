/**
 *
 */
package mz.org.fgh.disaapi.core.result.dao;

import java.time.LocalDateTime;
import java.util.List;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.model.Page;
import mz.org.fgh.disaapi.core.result.model.TypeOfResult;
import mz.org.fgh.disaapi.core.result.service.LabResultQueryService;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
public interface LabResultDAO {

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
			TypeOfResult typeOfResult,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			int pageNumber,
			int pageSize,
			String orderBy,
			String direction,
			EntityStatus entityStatus) throws BusinessException;
}
