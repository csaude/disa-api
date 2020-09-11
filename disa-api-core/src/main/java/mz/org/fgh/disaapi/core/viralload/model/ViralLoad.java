/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import mz.co.msaude.boot.frameworks.model.GenericEntity;
import mz.org.fgh.disaapi.core.viralload.dao.ViralLoadDAO;

/**
 * @author Stélio Moiane
 *
 */
@NamedQueries({
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus, query = ViralLoadDAO.QUERY.findByLocationCodeAndStatus),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findViralLoadByNid, query = ViralLoadDAO.QUERY.findViralLoadByNid) })
@Entity
@Table(name = "VlData")
public class ViralLoad extends GenericEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "UNIQUEID")
	private String nid;
	
	@Column(name = "RequestID")
	private String requestId;
	
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@Column(name = "SURNAME")
	private String lastName;
	
	@Column(name = "HL7SexCode")
	private char gender;
	
	@Column(name = "DOB")
	private LocalDate dateOfBirth;

	@Column(name = "RequestingFacilityCode")
	private String healthFacilityLabCode;

	@Column(name = "RequestingFacilityName")
	private String requestingFacilityName;

	@Column(name = "AttendingDoctor")
	private String nameOfTechnicianRequestingTest;

	@Column(name = "WARD")
	private String encounter;

	@Column(name = "Pregnant")
	private String pregnant;

	@Column(name = "BreastFeeding")
	private String breastFeeding;

	@Column(name = "ReasonForTest")
	private String reasonForTest;

	@Column(name = "SpecimenDatetime")
	private LocalDateTime harvestDate;

	@Column(name = "TypeOfSampleCollection")
	private String harvestType;

	@Column(name = "ReceivedDateTime")
	private LocalDateTime dateOfSampleReceive;

	@Column(name = "LIMSRejectionDesc")
	private String rejectedReason;

	@Column(name = "AnalysisDateTime")
	private LocalDateTime processingDate;

	@Column(name = "LIMSSpecimenSourceDesc")
	private String sampleType;

	@Column(name = "HIVVL_ViralLoadCAPCTM")
	private String viralLoadResultCopies;

	@Column(name = "HIVVL_VRLogValue")
	private String viralLoadResultLog;

	@Column(name = "ViralLoadResultCategory")
	private String viralLoadResultQualitative;

	@Column(name = "AuthorisedDateTime")
	private LocalDateTime viralLoadResultDate;

	@Column(name = "AuthorisedBy")
	private String aprovedBy;

	@Column(name = "ClinicalInfo")
	private String labComments;

	@Enumerated(EnumType.STRING)
	@Column(name = "VIRAL_LOAD_STATUS")
	private ViralLoadStatus viralLoadStatus;

	@Column(name = "HIVVL_ViralLoadResult")
	private String hivViralLoadResult;

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getNameOfTechnicianRequestingTest() {
		return nameOfTechnicianRequestingTest;
	}

	public void setNameOfTechnicianRequestingTest(String nameOfTechnicianRequestingTest) {
		this.nameOfTechnicianRequestingTest = nameOfTechnicianRequestingTest;
	}

	public String getHealthFacilityLabCode() {
		return healthFacilityLabCode;
	}

	public void setHealthFacilityLabCode(String healthFacilityLabCode) {
		this.healthFacilityLabCode = healthFacilityLabCode;
	}

	public String getEncounter() {
		return encounter;
	}

	public void setEncounter(String encounter) {
		this.encounter = encounter;
	}

	public String getPregnant() {
		return pregnant;
	}

	public void setPregnant(String pregnant) {
		this.pregnant = pregnant;
	}

	public String getBreastFeeding() {
		return breastFeeding;
	}

	public void setBreastFeeding(String breastFeeding) {
		this.breastFeeding = breastFeeding;
	}

	public String getReasonForTest() {
		return reasonForTest;
	}

	public void setReasonForTest(String reasonForTest) {
		this.reasonForTest = reasonForTest;
	}

	public String getRequestingFacilityName() {
		return requestingFacilityName;
	}

	public void setRequestingFacilityName(String requestingFacilityName) {
		this.requestingFacilityName = requestingFacilityName;
	}

	public String getRejectedReason() {
		return rejectedReason;
	}

	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}

	public String getHarvestType() {
		return harvestType;
	}

	public void setHarvestType(String harvestType) {
		this.harvestType = harvestType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getViralLoadResultCopies() {
		return viralLoadResultCopies;
	}

	public void setViralLoadResultCopies(String viralLoadResultCopies) {
		this.viralLoadResultCopies = viralLoadResultCopies;
	}

	public String getViralLoadResultLog() {
		return viralLoadResultLog;
	}

	public void setViralLoadResultLog(String viralLoadResultLog) {
		this.viralLoadResultLog = viralLoadResultLog;
	}

	public String getViralLoadResultQualitative() {
		return viralLoadResultQualitative;
	}

	public void setViralLoadResultQualitative(String viralLoadResultQualitative) {
		this.viralLoadResultQualitative = viralLoadResultQualitative;
	}

	public String getAprovedBy() {
		return aprovedBy;
	}

	public void setAprovedBy(String aprovedBy) {
		this.aprovedBy = aprovedBy;
	}

	public String getLabComments() {
		return labComments;
	}

	public void setLabComments(String labComments) {
		this.labComments = labComments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNid() {
		return this.nid;
	}

	public void setNid(final String nid) {
		this.nid = nid;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(final LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDateTime getHarvestDate() {
		return harvestDate;
	}

	public void setHarvestDate(LocalDateTime harvestDate) {
		this.harvestDate = harvestDate;
	}

	public LocalDateTime getDateOfSampleReceive() {
		return dateOfSampleReceive;
	}

	public void setDateOfSampleReceive(LocalDateTime dateOfSampleReceive) {
		this.dateOfSampleReceive = dateOfSampleReceive;
	}

	public LocalDateTime getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(LocalDateTime processingDate) {
		this.processingDate = processingDate;
	}

	public LocalDateTime getViralLoadResultDate() {
		return viralLoadResultDate;
	}

	public void setViralLoadResultDate(LocalDateTime viralLoadResultDate) {
		this.viralLoadResultDate = viralLoadResultDate;
	}

	public ViralLoadStatus getViralLoadStatus() {
		return viralLoadStatus;
	}

	public void setViralLoadStatus(ViralLoadStatus viralLoadStatus) {
		this.viralLoadStatus = viralLoadStatus;
	}

	public void setNotProcessed() {
		viralLoadStatus = ViralLoadStatus.NOT_PROCESSED;
	}

	public void setProcessed() {
		viralLoadStatus = ViralLoadStatus.PROCESSED;
	}

	public String getHivViralLoadResult() {
		return hivViralLoadResult;
	}

	public void setHivViralLoadResult(String hivViralLoadResult) {
		this.hivViralLoadResult = hivViralLoadResult;
	}
}
