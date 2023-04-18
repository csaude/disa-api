/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import static mz.org.fgh.disaapi.core.viralload.repository.ViralLoadSpecifications.createdInLocationBetweenDates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.ip.ImplementingPartner;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;
import mz.org.fgh.disaapi.core.viralload.repository.ViralLoadRepository;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Service
public class ViralLoadQueryServiceImpl implements ViralLoadQueryService {

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Inject
	private ViralLoadRepository viralLoadRepository;

	@Override
	public List<ViralLoad> findPendingByLocationCodeAndProvince(List<String> orgUnitCodes, String requestingProvinceName)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByLocationCodeAndStatus(orgUnitCodes, ViralLoadStatus.PENDING,
				EntityStatus.ACTIVE, requestingProvinceName);
	}

	@Override
	public List<ViralLoad> findPendingByLocationCode(List<String> orgUnitCodes) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByHealthFacilityLabCodeInAndViralLoadStatusAndEntityStatus(orgUnitCodes, ViralLoadStatus.PENDING,
				EntityStatus.ACTIVE);
	}

	@Override
	public Page<ViralLoad> findByForm(ViralLoad example, List<String> orgUnitCodes, LocalDateTime startDate,
			LocalDateTime endDate, Pageable pageable) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return viralLoadRepository.findAll(createdInLocationBetweenDates(example, orgUnitCodes, startDate, endDate),
				pageable);
	}

	@Override
	public mz.org.fgh.disaapi.core.viralload.model.Page<ViralLoad> findByForm(
			String requestId,
			String nid,
			List<String> orgUnitCodes,
			String referringRequestID,
			ViralLoadStatus viralLoadStatus,
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

		return this.viralLoadDAO.findByForm(requestId, nid,
				orgUnitCodes, referringRequestID, viralLoadStatus, notProcessingCause, startDate, endDate,
				search,
				pageNumber, pageSize, orderBy, direction, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findAllByForm(ViralLoad example, List<String> orgUnitCodes,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return viralLoadRepository.findAll(createdInLocationBetweenDates(example, orgUnitCodes, startDate, endDate));
	}

	@Override
	public List<ViralLoad> findAllByForm(String requestId, String nid, List<String> orgUnitCodes,
			String referringRequestID, ViralLoadStatus viralLoadStatus, NotProcessingCause notProcessingCause,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return this.viralLoadDAO.findAllByForm(
				requestId,
				nid,
				orgUnitCodes,
				referringRequestID,
				viralLoadStatus,
				notProcessingCause,
				startDate,
				endDate,
				DEFAULT_ORDER_BY,
				DEFAULT_DIRECTION,
				EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findViralLoadByNid(List<String> nids) throws BusinessException {

		if (nids.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByNidInAndEntityStatus(nids, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findViralLoadByRequestId(List<String> requestIds) throws BusinessException {
		if (requestIds.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByRequestIdInAndEntityStatus(requestIds, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findByLocaationCodeAndStatus(List<String> orgUnitCodes, ViralLoadStatus viralLoadStatus)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByHealthFacilityLabCodeInAndViralLoadStatusAndEntityStatus(orgUnitCodes, viralLoadStatus, EntityStatus.ACTIVE);
	}

	@Override
	public List<ViralLoad> findByLocationCodeAndStatusBetweenDates(List<String> orgUnitCodes, ViralLoadStatus viralLoadStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<ViralLoad>();
		}
		return viralLoadRepository.findByStatusAndDates(orgUnitCodes, viralLoadStatus, EntityStatus.ACTIVE, startDate,
				endDate);
	}
}
