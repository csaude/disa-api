package mz.org.fgh.disaapi.core.monitoring;

import java.time.LocalDateTime;

public class SyncMonitoringDetail {

    private String requestId;
    
    private String location;
    
    private LocalDateTime specimenDatetime;
    
    private LocalDateTime analysisDateTime;
    
    private LocalDateTime authorisedDateTime;
    
    private String requestingFacilityCode;
    
    private String requestingFacilityName;
    
    private String requestingProvinceName;
    
    private String requestingDistrictName;
    
    private String limsSpecimenSourceCode;
    
    private String entityStatus;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String viralLoadStatus;
    
    private String notProcessingCause;
    
    private String finalResult;
    
    private String typeOfResult;
    
    private String attribuite1;
    
    public SyncMonitoringDetail() {}

	public SyncMonitoringDetail(String requestId, String location, LocalDateTime specimenDatetime,
			LocalDateTime analysisDateTime, LocalDateTime authorisedDateTime, String requestingFacilityCode,
			String requestingFacilityName, String requestingProvinceName, String requestingDistrictName,
			String limsSpecimenSourceCode, String entityStatus, LocalDateTime createdAt, 
			LocalDateTime updatedAt, String viralLoadStatus, String notProcessingCause, String finalResult, 
			String typeOfResult, String attribuite1) {
		this.requestId = requestId;
		this.location = location;
		this.specimenDatetime = specimenDatetime;
		this.analysisDateTime = analysisDateTime;
		this.authorisedDateTime = authorisedDateTime;
		this.requestingFacilityCode = requestingFacilityCode;
		this.requestingFacilityName = requestingFacilityName;
		this.requestingProvinceName = requestingProvinceName;
		this.requestingDistrictName = requestingDistrictName;
		this.limsSpecimenSourceCode = limsSpecimenSourceCode;
		this.entityStatus = entityStatus;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.viralLoadStatus = viralLoadStatus;
		this.notProcessingCause = notProcessingCause;
		this.finalResult = finalResult;
		this.typeOfResult = typeOfResult;
		this.attribuite1 = attribuite1;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getSpecimenDatetime() {
		return specimenDatetime;
	}

	public void setSpecimenDatetime(LocalDateTime specimenDatetime) {
		this.specimenDatetime = specimenDatetime;
	}

	public LocalDateTime getAnalysisDateTime() {
		return analysisDateTime;
	}

	public void setAnalysisDateTime(LocalDateTime analysisDateTime) {
		this.analysisDateTime = analysisDateTime;
	}

	public LocalDateTime getAuthorisedDateTime() {
		return authorisedDateTime;
	}

	public void setAuthorisedDateTime(LocalDateTime authorisedDateTime) {
		this.authorisedDateTime = authorisedDateTime;
	}

	public String getRequestingFacilityCode() {
		return requestingFacilityCode;
	}

	public void setRequestingFacilityCode(String requestingFacilityCode) {
		this.requestingFacilityCode = requestingFacilityCode;
	}

	public String getRequestingFacilityName() {
		return requestingFacilityName;
	}

	public void setRequestingFacilityName(String requestingFacilityName) {
		this.requestingFacilityName = requestingFacilityName;
	}

	public String getRequestingProvinceName() {
		return requestingProvinceName;
	}

	public void setRequestingProvinceName(String requestingProvinceName) {
		this.requestingProvinceName = requestingProvinceName;
	}

	public String getRequestingDistrictName() {
		return requestingDistrictName;
	}

	public void setRequestingDistrictName(String requestingDistrictName) {
		this.requestingDistrictName = requestingDistrictName;
	}

	public String getLimsSpecimenSourceCode() {
		return limsSpecimenSourceCode;
	}

	public void setLimsSpecimenSourceCode(String limsSpecimenSourceCode) {
		this.limsSpecimenSourceCode = limsSpecimenSourceCode;
	}

	public String getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getViralLoadStatus() {
		return viralLoadStatus;
	}

	public void setViralLoadStatus(String viralLoadStatus) {
		this.viralLoadStatus = viralLoadStatus;
	}

	public String getNotProcessingCause() {
		return notProcessingCause;
	}

	public void setNotProcessingCause(String notProcessingCause) {
		this.notProcessingCause = notProcessingCause;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

	public String getTypeOfResult() {
		return typeOfResult;
	}

	public void setTypeOfResult(String typeOfResult) {
		this.typeOfResult = typeOfResult;
	}
	
	public String getAttribuite1() {
		return attribuite1;
	}
	
	public void setAttribuite1(String attribuite1) {
		this.attribuite1 = attribuite1;
	}
}
