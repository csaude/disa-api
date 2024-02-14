/**
 *
 */
package mz.org.fgh.disaapi.core.result.service;

import static mz.org.fgh.disaapi.core.result.repository.LabResultSpecifications.createdInLocationBetweenDates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.exception.NotFoundBusinessException;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.repository.LabResultRepository;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@Service
public class LabResultQueryServiceImpl implements LabResultQueryService {

	@Inject
	private LabResultRepository labResultRepository;

	@Override
	public List<LabResult> findPendingByLocationCodeAndProvince(List<String> orgUnitCodes,
			String requestingProvinceName)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<>();
		}
		return labResultRepository.findByLocationCodeAndStatus(orgUnitCodes, LabResultStatus.PENDING,
				EntityStatus.ACTIVE, requestingProvinceName);
	}

	@Override
	public List<LabResult> findPendingByLocationCode(List<String> orgUnitCodes) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {

			return new ArrayList<>();
		}
		return labResultRepository.findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(orgUnitCodes,
				LabResultStatus.PENDING,
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
	public List<LabResult> findAllByForm(LabResult example, List<String> orgUnitCodes,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			throw new BusinessException("The HF code should be informed");
		}

		return labResultRepository.findAll(createdInLocationBetweenDates(example, orgUnitCodes, startDate, endDate),
				Sort.by(Direction.DESC, "createdAt"));
	}

	@Override
	public List<LabResult> findViralLoadByNid(List<String> nids) throws BusinessException {

		if (nids.isEmpty()) {

			return new ArrayList<>();
		}
		return labResultRepository.findByNidInAndEntityStatus(nids, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findById(Long id) throws NotFoundBusinessException {
		LabResult labResult = labResultRepository.findByIdAndEntityStatus(id, EntityStatus.ACTIVE);
		if (labResult == null) {
			throw new NotFoundBusinessException();
		}
		return new ArrayList<>(Arrays.asList(labResult));
	}

	@Override
	public List<LabResult> findByLocaationCodeAndStatus(List<String> orgUnitCodes, LabResultStatus labResultStatus)
			throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<>();
		}
		return labResultRepository.findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(orgUnitCodes,
				labResultStatus, EntityStatus.ACTIVE);
	}

	@Override
	public List<LabResult> findByLocationCodeAndStatusBetweenDates(List<String> orgUnitCodes,
			LabResultStatus labResultStatus,
			LocalDateTime startDate, LocalDateTime endDate) throws BusinessException {

		if (orgUnitCodes.isEmpty()) {
			return new ArrayList<>();
		}
		return labResultRepository.findByStatusAndDates(orgUnitCodes, labResultStatus, EntityStatus.ACTIVE, startDate,
				endDate);
	}
}
