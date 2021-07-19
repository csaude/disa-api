/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
@Service(ViralLoadQueryServiceImpl.NAME)
public class ViralLoadQueryServiceImpl implements ViralLoadQueryService {

	public static final String NAME = "mz.org.fgh.disaapi.core.viralload.service.ViralLoadQueryServiceImpl";

	@Inject
	private ViralLoadDAO viralLoadDAO;

	@Override
	public List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes) throws BusinessException {

		if (locationCodes.isEmpty()) {

			return new ArrayList<ViralLoad>();
		}
		return this.viralLoadDAO.findByLocationCodeAndStatus(locationCodes, ViralLoadStatus.PENDING,
				EntityStatus.ACTIVE);
	}
	
	@Override
	public List<ViralLoad> findByForm(List<String> requestId) throws BusinessException {
		if (requestId.isEmpty()) {
			return new ArrayList<ViralLoad>();
		}
		return this.viralLoadDAO.findByForm(requestId, EntityStatus.ACTIVE);
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
