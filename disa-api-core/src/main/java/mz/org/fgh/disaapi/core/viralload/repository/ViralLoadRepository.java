package mz.org.fgh.disaapi.core.viralload.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mz.co.msaude.boot.frameworks.model.EntityStatus;
import mz.org.fgh.disaapi.core.viralload.model.NotProcessingCause;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoad;
import mz.org.fgh.disaapi.core.viralload.model.ViralLoadStatus;

public interface ViralLoadRepository extends JpaRepository<ViralLoad, Long>, JpaSpecificationExecutor<ViralLoad> {

    @Query("SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus AND vl.requestingProvinceName = :requestingProvinceName")
    List<ViralLoad> findByLocationCodeAndStatus(@Param("locationCodes") List<String> locationCodes,
            @Param("viralLoadStatus") ViralLoadStatus viralLoadStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("requestingProvinceName") String requestingProvinceName);

    List<ViralLoad> findByHealthFacilityLabCodeInAndViralLoadStatusAndEntityStatus(List<String> locationCodes,
            ViralLoadStatus viralLoadStatus,
            EntityStatus entityStatus);

    @Query("SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.createdAt between :startDate and :endDate")
    List<ViralLoad> findByStatusAndDates(@Param("locationCodes") List<String> locationCodes,
            @Param("viralLoadStatus") ViralLoadStatus viralLoadStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<ViralLoad> findByNidInAndEntityStatus(List<String> nids, EntityStatus entityStatus);

    List<ViralLoad> findByRequestIdInAndEntityStatus(List<String> requestIds, EntityStatus entityStatus);

    @Query("SELECT vl FROM ViralLoad vl WHERE vl.healthFacilityLabCode IN (:locationCodes) and vl.viralLoadStatus = :viralLoadStatus AND vl.entityStatus = :entityStatus and vl.notProcessingCause = :notProcessingCause")
    List<ViralLoad> findByLocationCodeStatusAndNotProcessingCause(@Param("locationCodes") List<String> locationCodes,
            @Param("viralLoadStatus") ViralLoadStatus viralLoadStatus,
            @Param("entityStatus") EntityStatus entityStatus,
            @Param("notProcessingCause") NotProcessingCause notProcessingCause);
}
