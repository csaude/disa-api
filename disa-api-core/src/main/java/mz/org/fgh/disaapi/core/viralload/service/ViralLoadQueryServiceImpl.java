/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.Page;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Service(ViralLoadQueryServiceImpl.NAME)
public class ViralLoadQueryServiceImpl implements ViralLoadQueryService {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryServiceImpl";

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, String requestingProvinceName) throws BusinessException {

		if (locationCodes.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return this.viralLoadDAO.findByLocationCodeAndStatus(locationCodes, ViralLoadStatus.PENDING,
				EntityStatus.ACTIVE, requestingProvinceName);
	}

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes) throws BusinessException {

		if (locationCodes.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return this.viralLoadDAO.findByLocationCodeAndStatus(locationCodes, ViralLoadStatus.PENDING,
				EntityStatus.ACTIVE);
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
			int pageNumber,
			int pageSize) throws BusinessException {

		// Should always start with page 1
		if (pageNumber == 0) {
			pageNumber = 1;
		}

		if (healthFacilityLabCode.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return this.viralLoadDAO.findByForm(requestId, nid,
				healthFacilityLabCode, referringRequestID, viralLoadStatus, notProcessingCause, startDate, endDate,
				pageNumber, pageSize, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findViralLoadByNid(List<String> nids) throws BusinessException {

		if (nids.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadDAO.findViralLoadByNid(nids, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findViralLoadByRequestId(List<String> requestIds) throws BusinessException {
		if (requestIds.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadDAO.findViralLoadByRequestId(requestIds, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findByStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus)
			throws BusinessException {

		if (locationCodes.isEmpty()) {
			return new ArrayList<ViralLoad>();
		}
		return viralLoadDAO.findByStatus(locationCodes, viralLoadStatus, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findByStatusAndDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (locationCodes.isEmpty()) {
			return new ArrayList<ViralLoad>();
		}
		return viralLoadDAO.findByStatusAndDates(locationCodes, viralLoadStatus, EntityStatus.ACTIVE, startDate,
				endDate);
	}
}
