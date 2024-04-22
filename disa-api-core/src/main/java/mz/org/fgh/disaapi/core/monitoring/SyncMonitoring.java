package mz.org.fgh.disaapi.core.monitoring;

import java.time.LocalDateTime;

public class SyncMonitoring {

    private String districtName;
    private String facilityName;
    private String facilityCode;
    private int totalReceived;
    private int totalProcessed;
    private int totalPending;
    private int totalNotProcessedNidNotFound;
    private int totalNotProcessedNoResult;
    private int totalNidDuplicate;
    private LocalDateTime lastCommunication;
    private int daysWithoutCommunication;
    private LocalDateTime lastResult;
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
