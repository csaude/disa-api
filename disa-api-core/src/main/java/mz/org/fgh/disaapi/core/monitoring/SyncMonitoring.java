package mz.org.fgh.disaapi.core.monitoring;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class SyncMonitoring {

    @Schema(description = "District name")
    private String districtName;

    @Schema(description = "HF name")
    private String facilityName;

    @Schema(description = "HF code")
    private String facilityCode;

    @Schema(description = "Total results received by the integration server")
    private int totalReceived;

    @Schema(description = "Total results processed from the integration server to SESP")
    private int totalProcessed;

    @Schema(description = "Total results pending synchronization from the integration server to SESP")
    private int totalPending;

    @Schema(description = "Total results of not processed due to NID not found in SESP")
    private int totalNotProcessedNidNotFound;

    @Schema(description = "Total results of not processed due to empty values")
    private int totalNotProcessedNoResult;

    @Schema(description = "Total results of not processed due to duplicate NID in SESP")
    private int totalNidDuplicate;

    @Schema(description = "Date of the last communication between SESP with the integration server")
    private LocalDateTime lastCommunication;

    @Schema(description = "Number of days since last communication between SESP with the integration server to the current date")
    private int daysWithoutCommunication;

    @Schema(description = "Date of the last result loaded into the Integration Server")
    private LocalDateTime lastResult;

    @Schema(description = "Number of days since the last result loaded into the integration server to the current date")
    private int daysWithoutResult;

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public int getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(int totalReceived) {
        this.totalReceived = totalReceived;
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public int getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public int getTotalNotProcessedNidNotFound() {
        return totalNotProcessedNidNotFound;
    }

    public void setTotalNotProcessedNidNotFound(int totalNotProcessedNidNotFound) {
        this.totalNotProcessedNidNotFound = totalNotProcessedNidNotFound;
    }

    public int getTotalNotProcessedNoResult() {
        return totalNotProcessedNoResult;
    }

    public void setTotalNotProcessedNoResult(int totalNotProcessedNoResult) {
        this.totalNotProcessedNoResult = totalNotProcessedNoResult;
    }

    public int getTotalNidDuplicate() {
        return totalNidDuplicate;
    }

    public void setTotalNidDuplicate(int totalNidDuplicate) {
        this.totalNidDuplicate = totalNidDuplicate;
    }

    public LocalDateTime getLastCommunication() {
        return lastCommunication;
    }

    public void setLastCommunication(LocalDateTime lastCommunication) {
        this.lastCommunication = lastCommunication;
    }

    public int getDaysWithoutCommunication() {
        return daysWithoutCommunication;
    }

    public void setDaysWithoutCommunication(int daysWithoutCommunication) {
        this.daysWithoutCommunication = daysWithoutCommunication;
    }

    public LocalDateTime getLastResult() {
        return lastResult;
    }

    public void setLastResult(LocalDateTime lastResult) {
        this.lastResult = lastResult;
    }

    public int getDaysWithoutResult() {
        return daysWithoutResult;
    }

    public void setDaysWithoutResult(int daysWithoutResult) {
        this.daysWithoutResult = daysWithoutResult;
    }

}
