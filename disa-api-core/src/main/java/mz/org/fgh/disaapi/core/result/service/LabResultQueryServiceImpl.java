/**
 *
 */
package mz.org.fgh.disaapi.core.result.service;

import static mz.org.fgh.disaapi.core.result.repository.LabResultSpecifications.createdInLocationBetweenDates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.result.dao.LabResultDAO;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Service
public class LabResultQueryServiceImpl implements LabResultQueryService {

	@Inject
	private LabResultDAO labResultDAO;

	@Inject
	private LabResultRepository labResultRepository;

	@Override
	public List<LabResult> findPendingByLocationCodeAndProvince(List<String> orgUnitCodes, String requestingProvinceName)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByLocationCodeAndStatus(orgUnitCodes, LabResultStatus.PENDING,
				EntityStatus.ACTIVE, requestingProvinceName);
	}

	@Override
	public List<LabResult> findPendingByLocationCode(List<String> orgUnitCodes) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(orgUnitCodes, LabResultStatus.PENDING,
				EntityStatus.ACTIVE);
	}

	@Override
	public Page<LabResult> findByForm(LabResult example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return labResultRepository.findAll(createdInLocationBetweenDates(example, orgUnitCodes, startDate, endDate),
				pageable);
	}

	@Override
	public mz.org.fgh.disaapi.core.result.model.Page<LabResult> findByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String referringRequestID,
			LabResultStatus labResultStatus,
			NotProcessingCause notProcessingCause,
			LocalDateTime startDate,
			LocalDateTime endDate,
			String search,
			int pageNumber,
			int pageSize,
			String orderBy,
			String direction) throws BusinessException {

		// Should always start with page 1
		if (pageNumber == 0) {
			pageNumber = 1;
		}

		if (pageSize == 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}

		if (pageSize > MAX_PAGE_SIZE) {
			pageSize = MAX_PAGE_SIZE;
		}

		// If no order by order, use DEFAULT_ORDER_BY and DEFAULT_DIRECTION
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = DEFAULT_ORDER_BY;
			direction = DEFAULT_DIRECTION;

			// If order by but no direction, sort ASCENDING
		} else if (StringUtils.isEmpty(direction)) {
			direction = ASCENDING;
		}

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return this.labResultDAO.findByForm(requestId, nid,
				orgUnitCodes, referringRequestID, labResultStatus, notProcessingCause, startDate, endDate,
				search,
				pageNumber, pageSize, orderBy, direction, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findAllByForm(LabResult example, List<String> orgUnitCodes,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return labResultRepository.findAll(createdInLocationBetweenDates(example, orgUnitCodes, startDate, endDate));
	}

	@Override
	public List<LabResult> findAllByForm(String requestId, String nid, List<String> orgUnitCodes,
			String referringRequestID, LabResultStatus labResultStatus, NotProcessingCause notProcessingCause,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return this.labResultDAO.findAllByForm(
				requestId,
				nid,
				orgUnitCodes,
				referringRequestID,
				labResultStatus,
				notProcessingCause,
				startDate,
				endDate,
				DEFAULT_ORDER_BY,
				DEFAULT_DIRECTION,
				EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findViralLoadByNid(List<String> nids) throws BusinessException {

		if (nids.isEmpty()) {

			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByNidInAndEntityStatus(nids, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findViralLoadByRequestId(List<String> requestIds) throws BusinessException {
		if (requestIds.isEmpty()) {

			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByRequestIdInAndEntityStatus(requestIds, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findByLocaationCodeAndStatus(List<String> orgUnitCodes, LabResultStatus labResultStatus)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(orgUnitCodes, labResultStatus, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findByLocationCodeAndStatusBetweenDates(List<String> orgUnitCodes, LabResultStatus labResultStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<LabResult>();
		}
		return labResultRepository.findByStatusAndDates(orgUnitCodes, labResultStatus, EntityStatus.ACTIVE, startDate,
				endDate);
	}
}
