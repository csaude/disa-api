package mz.org.fgh.disaapi.core.result.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.result.model.LabResult;
import mz.org.fgh.disaapi.core.result.model.LabResultStatus;
import mz.org.fgh.disaapi.core.result.model.NotProcessingCause;

public interface LabResultRepository extends JpaRepository<LabResult, Long>, JpaSpecificationExecutor<LabResult> {

    @Query("SELECT vl FROM LabResult vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.labResultStatus = :labResultStatus AND vl.entityStatus = :entityStatus AND vl.requestingProvinceName = :requestingProvinceName")
    List<LabResult> findByLocationCodeAndStatus(@Param("locationCodes") List<String> locationCodes,
            @Param("labResultStatus") LabResultStatus labResultStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("requestingProvinceName") String requestingProvinceName);

    List<LabResult> findByHealthFacilityLabCodeInAndLabResultStatusAndEntityStatus(List<String> locationCodes,
            LabResultStatus labResultStatus,
            EntityStatus entityStatus);

    @Query("SELECT vl FROM LabResult vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.labResultStatus = :labResultStatus AND vl.entityStatus = :entityStatus and vl.createdAt between :startDate and :endDate")
    List<LabResult> findByStatusAndDates(@Param("locationCodes") List<String> locationCodes,
            @Param("labResultStatus") LabResultStatus labResultStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<LabResult> findByNidInAndEntityStatus(List<String> nids, EntityStatus entityStatus);

    List<LabResult> findByRequestIdInAndEntityStatus(List<String> requestIds, EntityStatus entityStatus);

    @Query("SELECT vl FROM LabResult vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.labResultStatus = :labResultStatus AND vl.entityStatus = :entityStatus and vl.notProcessingCause = :notProcessingCause")
    List<LabResult> findByLocationCodeStatusAndNotProcessingCause(@Param("locationCodes") List<String> locationCodes,
            @Param("labResultStatus") LabResultStatus labResultStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("notProcessingCause") NotProcessingCause notProcessingCause);

    LabResult findByIdAndEntityStatus(Long id, EntityStatus active);
}
