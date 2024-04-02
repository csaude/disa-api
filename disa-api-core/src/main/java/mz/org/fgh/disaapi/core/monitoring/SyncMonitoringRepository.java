package mz.org.fgh.disaapi.core.monitoring;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
                "       indicators.facilityCode, " +
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
                "  (SELECT RequestingDistrictName as districtName, " +
                "          RequestingFacilityName as facilityName, " +
                "          RequestingFacilityCode as facilityCode " +
                "   FROM VlData " +
                "   WHERE RequestingFacilityCode in (:locationCodes) " +
                "   GROUP BY RequestingDistrictName, " +
                "            RequestingFacilityName, " +
                "            RequestingFacilityCode " +
                "   ORDER BY 1, " +
                "            2 ASC) location " +
                "LEFT JOIN " +
                "  (SELECT RequestingFacilityCode as facilityCode, " +
                "          COUNT(*) as totalReceived, " +
                "          COUNT(IF(VIRAL_LOAD_STATUS='PROCESSED', 1, NULL)) totalProcessed, " +
                "          COUNT(IF(VIRAL_LOAD_STATUS='PENDING', 1, NULL)) totalPending, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='NID_NOT_FOUND' " +
                "                   AND VIRAL_LOAD_STATUS<>'PROCESSED', 1, NULL)) totalNotProcessedNidNotFound, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='NO_RESULT' " +
                "                   AND VIRAL_LOAD_STATUS<>'PROCESSED', 1, NULL)) totalNotProcessedNoResult, " +
                "          COUNT(IF(NOT_PROCESSING_CAUSE='DUPLICATE_NID' " +
                "                   AND VIRAL_LOAD_STATUS<>'PROCESSED', 1, NULL)) totalNidDuplicate, " +
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
            s.setTotalReceived(Integer.parseInt(String.valueOf(result[RECEIVED])));
            s.setTotalProcessed(Integer.parseInt(String.valueOf(result[PROCESSED])));
            s.setTotalPending(Integer.parseInt(String.valueOf(result[PENDING])));
            s.setTotalNotProcessedNidNotFound(Integer.parseInt(String.valueOf(result[NOT_PROCESED_NID_NOT_FOUND])));
            s.setTotalNotProcessedNoResult(Integer.parseInt(String.valueOf(result[NOT_PROCESSED_NO_RESULT])));
            s.setTotalNidDuplicate(Integer.parseInt(String.valueOf(result[DUPLICATE_NID])));
            s.setLastCommunication(LocalDateTime.parse(String.valueOf(result[LAST_COMMUNICATION]), pattern));
            s.setDaysWithoutCommunication(Integer.parseInt(String.valueOf(result[DAYS_WITHOUT_COMMUNICATION])));
            s.setLastResult(LocalDateTime.parse(String.valueOf(result[LAST_RESULT]), pattern));
            s.setDaysWithoutResult(Integer.parseInt(String.valueOf(result[DAYS_WITHOUT_RESULT])));
            monitoringList.add(s);
        }

        return monitoringList;
    }
}
