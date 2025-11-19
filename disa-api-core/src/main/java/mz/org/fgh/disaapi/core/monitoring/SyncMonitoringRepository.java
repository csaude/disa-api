package mz.org.fgh.disaapi.core.monitoring;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import mz.org.fgh.disaapi.core.ip.ImplementingPartner;

@Component
public class SyncMonitoringRepository {

    private static final int DISTRICT_NAME = 0;
    private static final int FACILITY_NAME = 1;
    private static final int FACILITY_CODE = 2;
    private static final int RECEIVED = 3;
    private static final int PROCESSED = 4;
    private static final int PENDING = 5;
    private static final int NOT_PROCESED_NID_NOT_FOUND = 6;
    private static final int NOT_PROCESSED_NO_RESULT = 7;
    private static final int DUPLICATE_NID = 8;
    private static final int LAST_COMMUNICATION = 9;
    private static final int DAYS_WITHOUT_COMMUNICATION = 10;
    private static final int LAST_RESULT = 11;
    private static final int DAYS_WITHOUT_RESULT = 12;
    
    // constants for disaggregated data query
    private static final int DETAIL_REQUEST_ID = 0;
    private static final int DETAIL_LOCATION = 1;
    private static final int DETAIL_NID = 2;
    private static final int DETAIL_LASTNAME = 3;
    private static final int DETAIL_FIRSTNAME = 4;
    private static final int DETAIL_GENDER = 5;
    private static final int DETAIL_SPECIMEN_DATE_TIME = 6;
    private static final int DETAIL_ANALYSIS_DATETIME = 7;
    private static final int DETAIL_AUTHORISED_DATETIME = 8;
    private static final int DETAIL_REQUESTING_FACILITYCODE = 9;
    private static final int DETAIL_REQUESTING_FACILITYNAME = 10;
    private static final int DETAIL_REQUESTING_PROVINCENAME = 11;
    private static final int DETAIL_REQUESTING_DISTRICTNAME = 12;
    private static final int DETAIL_LIMSSPECIMEN_SOURCECODE = 13;
    private static final int DETAIL_HIV_VL_VIRAL_LOAD_RESULT = 14;
    private static final int DETAIL_HIVVLVIRALLOADCAPCTM = 15;
    private static final int DETAIL_ENTITYSTATUS = 16;
    private static final int DETAIL_CREATEDAT = 17;
    private static final int DETAIL_UPDATEDAT = 18;
    private static final int DETAIL_VIRALLOADSTATUS = 19;
    private static final int DETAIL_NOTPROCESSINGCAUSE = 20;
    private static final int DETAIL_FINALRESULT = 21;
    private static final int DETAIL_TYPEOFRESULT = 22;

    private EntityManager entityManager;

    public SyncMonitoringRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<SyncMonitoring> getSyncMonitoring() {

        ImplementingPartner partner = (ImplementingPartner) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        String query = "SELECT location.districtName, " +
                "       location.facilityName, " +
                "       location.facilityCode, " +
                "       indicators.totalReceived, " +
                "       indicators.totalProcessed, " +
                "       indicators.totalPending, " +
                "       indicators.totalNotProcessedNidNotFound, " +
                "       indicators.totalNotProcessedNoResult, " +
                "       indicators.totalNidDuplicate, " +
                "       indicators.lastCommunication, " +
                "       indicators.daysWithoutCommunication, " +
                "       indicators.lastResultLoad, " +
                "       indicators.daysWithoutResultLoad " +
                "FROM " +
                "  (SELECT district as districtName, " +
                "          facility as facilityName, " +
                "          code as facilityCode " +
                "   FROM OrgUnit " +
                "   WHERE code in (:locationCodes) " +
                "   GROUP BY district, " +
                "            facility, " +
                "            code " +
                "   ORDER BY 1, " +
                "            2 ASC) location " +
                "LEFT JOIN " +
                "  (SELECT RequestingFacilityCode as facilityCode, " +
                "          COUNT(*) as totalReceived, " +
                "          COUNT(IF(VIRAL_LOAD_STATUS='PROCESSED', 1, NULL)) totalProcessed, " +
                "          COUNT(IF(VIRAL_LOAD_STATUS='PENDING', 1, NULL)) totalPending, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='NID_NOT_FOUND' " +
                "                   AND VIRAL_LOAD_STATUS='NOT_PROCESSED', 1, NULL)) totalNotProcessedNidNotFound, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='NO_RESULT' " +
                "                   AND VIRAL_LOAD_STATUS='NOT_PROCESSED', 1, NULL)) totalNotProcessedNoResult, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='DUPLICATE_NID' " +
                "                   AND VIRAL_LOAD_STATUS='NOT_PROCESSED', 1, NULL)) totalNidDuplicate, " +
                "          MAX(UPDATED_AT) as lastCommunication, " +
                "          (to_days(cast(now() as date)) - to_days(cast(MAX(UPDATED_AT) as date))) AS daysWithoutCommunication, "
                +
                "          MAX(CREATED_AT) as lastResultLoad, " +
                "          (to_days(cast(now() as date)) - to_days(cast(MAX(CREATED_AT) as date))) AS daysWithoutResultLoad "
                +
                "   FROM VlData " +
                "   WHERE RequestingFacilityCode in (:locationCodes) " +
                "     AND ENTITY_STATUS='ACTIVE' " +
                "   GROUP BY RequestingFacilityCode) indicators ON location.facilityCode=indicators.facilityCode; ";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("locationCodes", partner.getOrgUnitCodes());

        DateTimeFormatter pattern = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
        List<Object[]> resultList = nativeQuery.getResultList();
        List<SyncMonitoring> monitoringList = new ArrayList<>();
        for (Object[] result : resultList) {
            SyncMonitoring s = new SyncMonitoring();
            s.setDistrictName(String.valueOf(result[DISTRICT_NAME]));
            s.setFacilityName(String.valueOf(result[FACILITY_NAME]));
            s.setFacilityCode(String.valueOf(result[FACILITY_CODE]));

            if (result[RECEIVED] != null) {
                s.setTotalReceived(Integer.parseInt(String.valueOf(result[RECEIVED])));
            }

            if (result[PROCESSED] != null) {
                s.setTotalProcessed(Integer.parseInt(String.valueOf(result[PROCESSED])));
            }
            if (result[PENDING] != null) {
                s.setTotalPending(Integer.parseInt(String.valueOf(result[PENDING])));
            }
            if (result[NOT_PROCESED_NID_NOT_FOUND] != null) {
                s.setTotalNotProcessedNidNotFound(Integer.parseInt(String.valueOf(result[NOT_PROCESED_NID_NOT_FOUND])));
            }
            if (result[NOT_PROCESSED_NO_RESULT] != null) {
                s.setTotalNotProcessedNoResult(Integer.parseInt(String.valueOf(result[NOT_PROCESSED_NO_RESULT])));
            }
            if (result[DUPLICATE_NID] != null) {
                s.setTotalNidDuplicate(Integer.parseInt(String.valueOf(result[DUPLICATE_NID])));
            }
            if (result[LAST_COMMUNICATION] != null) {
                s.setLastCommunication(LocalDateTime.parse(String.valueOf(result[LAST_COMMUNICATION]), pattern));
            }
            if (result[DAYS_WITHOUT_COMMUNICATION] != null) {
                s.setDaysWithoutCommunication(Integer.parseInt(String.valueOf(result[DAYS_WITHOUT_COMMUNICATION])));
            }
            if (result[LAST_RESULT] != null) {
                s.setLastResult(LocalDateTime.parse(String.valueOf(result[LAST_RESULT]), pattern));
            }
            if (result[DAYS_WITHOUT_RESULT] != null) {
                s.setDaysWithoutResult(Integer.parseInt(String.valueOf(result[DAYS_WITHOUT_RESULT])));
            }
            monitoringList.add(s);
        }

        return monitoringList;
    }
    
    @SuppressWarnings("unchecked")
	public List<SyncMonitoringDetail> getSyncMonitoringDetail() {
    	
    	ImplementingPartner partner = (ImplementingPartner) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
    	
    	String query = "SELECT vl.RequestID as requestId, " +
    			"vl.LOCATION as location, " +
    			"vl.UNIQUEID as nid, " +
    			"vl.SURNAME as surname, " +
    			"vl.FIRSTNAME as firstname, " +
    			"vl.HL7SexCode as gender, " +
    			"vl.SpecimenDatetime as specimenDatetime, " +
    			"vl.AnalysisDateTime as analysisDateTime, " +
    			"vl.AuthorisedDateTime as authorisedDateTime, " +
    			"vl.RequestingFacilityCode as requestingFacilityCode, " +
    			"vl.RequestingFacilityName as requestingFacilityName, " +
    			"vl.RequestingProvinceName as requestingProvinceName, " +
    			"vl.RequestingDistrictName as requestingDistrictName, " +
    			"vl.LIMSSpecimenSourceCode as limsSpecimenSourceCode, "+
    			"vl.HIVVL_ViralLoadResult as hivVlViralLoadResult, "+
    			"vl.HIVVL_ViralLoadCAPCTM as hivVlViralLoadCapCtm, " +
    			"vl.ENTITY_STATUS as entityStatus, " +
    			"vl.CREATED_AT as createdAt, " +
    			"vl.UPDATED_AT as updatedAt, " +
    			"vl.VIRAL_LOAD_STATUS as viralLoadStatus, " +
    			"vl.NOT_PROCESSING_CAUSE as notProcessingCause, " +
    			"vl.FinalResult as finalResult, " +
    			"vl.TypeOfResult as typeOfResult " +
                "FROM VlData vl " +
                "INNER JOIN OrgUnit org ON vl.RequestingFacilityCode = org.code " +
                "WHERE vl.RequestingFacilityCode in (:locationCodes) " +
                "AND vl.ENTITY_STATUS = 'ACTIVE' " +
                "ORDER BY org.district, org.facility, vl.UPDATED_AT DESC";
    	
    	Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter("locationCodes", partner.getOrgUnitCodes());

        DateTimeFormatter pattern = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
        
        List<Object[]> resultList = nativeQuery.getResultList();
        
        List<SyncMonitoringDetail> detailList = new ArrayList<>();
        
        for(Object[] result : resultList) {
        	SyncMonitoringDetail detail = new SyncMonitoringDetail();
        	
        	if (result[DETAIL_REQUEST_ID] != null) {
        		detail.setRequestId(String.valueOf(result[DETAIL_REQUEST_ID]));
        	}

        	if (result[DETAIL_LOCATION] != null) {
        		detail.setLocation(String.valueOf(result[DETAIL_LOCATION]));
        	}

        	if (result[DETAIL_NID] != null) {
        		detail.setNid(String.valueOf(result[DETAIL_NID]));
        	}

        	if (result[DETAIL_LASTNAME] != null) {
        		detail.setLastName(String.valueOf(result[DETAIL_LASTNAME]));
        	}

        	if (result[DETAIL_FIRSTNAME] != null) {
        		detail.setFirstName(String.valueOf(result[DETAIL_FIRSTNAME]));
        	}

        	if (result[DETAIL_GENDER] != null) {
        		detail.setGender(String.valueOf(result[DETAIL_GENDER]));
        	}

        	if (result[DETAIL_SPECIMEN_DATE_TIME] != null) {
        		detail.setSpecimenDatetime(LocalDateTime.parse(String.valueOf(result[DETAIL_SPECIMEN_DATE_TIME]), pattern));
        	}

        	if (result[DETAIL_ANALYSIS_DATETIME] != null) {
        		detail.setAnalysisDateTime(LocalDateTime.parse(String.valueOf(result[DETAIL_ANALYSIS_DATETIME]), pattern));
        	}
        	
        	if (result[DETAIL_AUTHORISED_DATETIME] != null) {
        		detail.setAuthorisedDateTime(LocalDateTime.parse(String.valueOf(result[DETAIL_AUTHORISED_DATETIME]), pattern));
        	}
        	
        	if (result[DETAIL_REQUESTING_FACILITYCODE] != null) {
        		detail.setRequestingFacilityCode(String.valueOf(result[DETAIL_REQUESTING_FACILITYCODE]));
        	}
        	
        	if (result[DETAIL_REQUESTING_FACILITYNAME] != null) {
        		detail.setRequestingFacilityName(String.valueOf(result[DETAIL_REQUESTING_FACILITYNAME]));
        	}

        	if (result[DETAIL_REQUESTING_PROVINCENAME] != null) {
        		detail.setRequestingProvinceName(String.valueOf(result[DETAIL_REQUESTING_PROVINCENAME]));
        	}
        	
        	if (result[DETAIL_REQUESTING_DISTRICTNAME] != null) {
        		detail.setRequestingDistrictName(String.valueOf(result[DETAIL_REQUESTING_DISTRICTNAME]));
        	}
        	
        	if (result[DETAIL_LIMSSPECIMEN_SOURCECODE] != null) {
        		detail.setLimsSpecimenSourceCode(String.valueOf(result[DETAIL_LIMSSPECIMEN_SOURCECODE]));
        	}
        	
        	if (result[DETAIL_HIV_VL_VIRAL_LOAD_RESULT] != null) {
        		detail.setHivVlViralLoadResult(String.valueOf(result[DETAIL_HIV_VL_VIRAL_LOAD_RESULT]));
        	}
        	
        	if(result[DETAIL_HIVVLVIRALLOADCAPCTM] != null) {
        		detail.setHivVlViralLoadCapCtm(String.valueOf(result[DETAIL_HIVVLVIRALLOADCAPCTM]));
        	}
        	
        	if(result[DETAIL_ENTITYSTATUS] != null) {
        		detail.setEntityStatus(String.valueOf(result[DETAIL_ENTITYSTATUS]));  
        	}
        	
        	if(result[DETAIL_CREATEDAT] != null) {
        		detail.setCreatedAt(LocalDateTime.parse(String.valueOf(result[DETAIL_CREATEDAT]), pattern));    
        	}
        	
        	if(result[DETAIL_UPDATEDAT] != null) {
        		detail.setUpdatedAt(LocalDateTime.parse(String.valueOf(result[DETAIL_UPDATEDAT]), pattern));  
        	}
        	
        	if(result[DETAIL_VIRALLOADSTATUS] != null) {
        		detail.setViralLoadStatus(String.valueOf(result[DETAIL_VIRALLOADSTATUS]));  
        	}
        	
        	if(result[DETAIL_NOTPROCESSINGCAUSE] != null) {
        		detail.setNotProcessingCause(String.valueOf(result[DETAIL_NOTPROCESSINGCAUSE])); 
        	}
        	
        	if(result[DETAIL_FINALRESULT] != null) {
        		detail.setFinalResult(String.valueOf(result[DETAIL_FINALRESULT]));   
        	}
        	
        	if(result[DETAIL_TYPEOFRESULT] != null) {
        		detail.setTypeOfResult(String.valueOf(result[DETAIL_TYPEOFRESULT]));
        	}
        	
        	detailList.add(detail);
        }
      
        return detailList;        
    }
}
