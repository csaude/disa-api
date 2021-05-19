/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.dao;

import java.time.LocalDateTime;
import java.util.List;

import mz.co.msaude.boot.frameworks.dao.GenericDAO;
import mz.co.msaude.boot.frameworks.exception.BusinessException;
import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

/**
 * @author Stélio Moiane
 *
 */
public interface ViralLoadDAO extends GenericDAO<ViralLoad, Long> {
	class QUERY {
		public static final String findByLocationCodeAndStatus = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus";
		public static final String findByStatusAndDates = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.createdAt between :startDate and :endDate";
		public static final String findViralLoadByNid = "SELECT vl FROM ViralLoad vl WHERE vl.nid IN (:nids) AND vl.entityStatus = :entityStatus";
		public static final String findViralLoadByRequestId = "SELECT vl FROM ViralLoad vl WHERE vl.requestId IN (:requestIds) AND vl.entityStatus = :entityStatus";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes)and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.notProcessingCause = :notProcessingCause";

	}

	class QUERY_NAME {
		public static final String findByLocationCodeAndStatus = "ViralLoad.findByLocationCodeAndStatus";
		public static final String findByStatusAndDates = "ViralLoad.findByStatusAndDates";
		public static final String findViralLoadByNid = "ViralLoad.findViralLoadByNid";
		public static final String findViralLoadByRequestId = "ViralLoad.findViralLoadByRequestId";
		public static final String findByLocationCodeStatusAndNotProcessingCause = "ViralLoad.findByLocationCodeStatusAndNotProcessingCause";

	}

	List<ViralLoad> findByLocationCodeAndStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus) throws BusinessException;

	List<ViralLoad> findByStatus(List<String> locationCodes, ViralLoadStatus viralLoadStatus, EntityStatus entityStatus)
			throws BusinessException;

	List<ViralLoad> findByStatusAndDates(List<String> locationCodes, ViralLoadStatus viralLoadStatus,
			EntityStatus entityStatus, LocalDateTime startDate, LocalDateTime endDate) throws BusinessException;

	List<ViralLoad> findViralLoadByNid(List<String> nids, EntityStatus entityStatus) throws BusinessException;
	
	List<ViralLoad> findViralLoadByRequestId(List<String> requestIds, EntityStatus entityStatus) throws BusinessException;

	List<ViralLoad> findByLocationCodeStatusAndNotProcessingCause(List<String> locationCodes,
			ViralLoadStatus viralLoadStatus, EntityStatus entityStatus, NotProcessingCause reasonForNotProcessing)
			throws BusinessException;

}
