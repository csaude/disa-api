/**
 *
 */
package mz.org.fgh.disaapi.core.viralload.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Basic;
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
 * @author Hélio Machabane
 *
 */
@NamedQueries({
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatus, query = ViralLoadDAO.QUERY.findByLocationCodeAndStatus),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findByLocationCodeAndStatusSimple, query = ViralLoadDAO.QUERY.findByLocationCodeAndStatusSimple),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findViralLoadByNid, query = ViralLoadDAO.QUERY.findViralLoadByNid),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findViralLoadByRequestId, query = ViralLoadDAO.QUERY.findViralLoadByRequestId),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findByStatusAndDates, query = ViralLoadDAO.QUERY.findByStatusAndDates),
		@NamedQuery(name = ViralLoadDAO.QUERY_NAME.findByLocationCodeStatusAndNotProcessingCause, query = ViralLoadDAO.QUERY.findByLocationCodeStatusAndNotProcessingCause) })
@Entity
@Table(name = "VlData")
public class ViralLoad extends GenericEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "UNIQUEID")
	private String nid;

	@Column(name = "RequestID", unique = true)
	private String requestId;

	@Column(name = "ReferringRequestID")
	private String referringRequestID;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "SURNAME")
	private String lastName;

	@Column(name = "HL7SexCode")
	private String gender;

	@Column(name = "DOB")
	private LocalDate dateOfBirth;

	@Column(name = "LOCATION")
	private String location;

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

	@Column(name = "LIMSSpecimenSourceCode")
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

	@Basic(optional = false)
	@Column(name = "VIRAL_LOAD_STATUS", columnDefinition = "enum('PENDING','PROCESSED','NOT_PROCESSED')")
	@Enumerated(EnumType.STRING)
	private ViralLoadStatus viralLoadStatus;

	@Column(name = "HIVVL_ViralLoadResult")
	private String hivViralLoadResult;

	@Enumerated(EnumType.STRING)
	@Column(name = "NOT_PROCESSING_CAUSE")
	private NotProcessingCause notProcessingCause;

	@Column(name = "LastViralLoadResult")
	private String lastViralLoadResult;

	@Column(name = "LastViralLoadDate")
	private String lastViralLoadDate;

	@Column(name = "ARTRegimen")
	private String artRegimen;

	@Column(name = "PrimeiraLinha")
	private String primeiraLinha;

	@Column(name = "SegundaLinha")
	private String segundaLinha;

	@Column(name = "DataDeInicioDoTARV")
	private String dataDeInicioDoTARV;

	@Column(name = "RequestingProvinceName")
	private String requestingProvinceName;

	@Column(name = "RequestingDistrictName")
	private String requestingDistrictName;

	@Column(name = "FinalViralLoadResult")
	private String finalViralLoadResult;

	@Column(name = "IsPoc")
	private byte isPoc;

	@Column(name = "SYNCHRONIZED_BY")
	private String synchronizedBy;

	@Column
	private Integer ageInYears;

	public String getSynchronizedBy() {
		return synchronizedBy;
	}

	public void setSynchronizedBy(String synchronizedBy) {
		this.synchronizedBy = synchronizedBy;
	}

	public byte getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(byte isPoc) {
		this.isPoc = isPoc;
	}

	public String getFinalViralLoadResult() {
		return finalViralLoadResult;
	}

	public void setFinalViralLoadResult(String finalViralLoadResult) {
		this.finalViralLoadResult = finalViralLoadResult;
	}

	public String getDataDeInicioDoTARV() {
		return dataDeInicioDoTARV;
	}

	public void setDataDeInicioDoTARV(String dataDeInicioDoTARV) {
		this.dataDeInicioDoTARV = dataDeInicioDoTARV;
	}

	public String getPrimeiraLinha() {
		return primeiraLinha;
	}

	public void setPrimeiraLinha(String primeiraLinha) {
		this.primeiraLinha = primeiraLinha;
	}

	public String getSegundaLinha() {
		return segundaLinha;
	}

	public void setSegundaLinha(String segundaLinha) {
		this.segundaLinha = segundaLinha;
	}

	public String getArtRegimen() {
		return artRegimen;
	}

	public void setArtRegimen(String artRegimen) {
		this.artRegimen = artRegimen;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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

	public void setPending() {
		viralLoadStatus = ViralLoadStatus.PENDING;
	}

	public String getHivViralLoadResult() {
		return hivViralLoadResult;
	}

	public void setHivViralLoadResult(String hivViralLoadResult) {
		this.hivViralLoadResult = hivViralLoadResult;
	}

	public NotProcessingCause getNotProcessingCause() {
		return notProcessingCause;
	}

	public void setNotProcessingCause(NotProcessingCause notProcessingCause) {
		this.notProcessingCause = notProcessingCause;
	}

	public void setCauseNoNID() {
		notProcessingCause = NotProcessingCause.NID_NOT_FOUND;
	}

	public void setCauseNoResult() {
		notProcessingCause = NotProcessingCause.NO_RESULT;
	}

	public void setCauseFlaggedForReview() {
		notProcessingCause = NotProcessingCause.FLAGGED_FOR_REVIEW;
	}

	public void setCauseDuplicateNid() {
		notProcessingCause = NotProcessingCause.DUPLICATE_NID;
	}

	public void setCauseDuplicateReqId() {
		notProcessingCause = NotProcessingCause.DUPLICATED_REQUEST_ID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getReferringRequestID() {
		return referringRequestID;
	}

	public void setReferringRequestID(String referringRequestID) {
		this.referringRequestID = referringRequestID;
	}

	public String getLastViralLoadResult() {
		return lastViralLoadResult;
	}

	public void setLastViralLoadResult(String lastViralLoadResult) {
		this.lastViralLoadResult = lastViralLoadResult;
	}

	public String getLastViralLoadDate() {
		return lastViralLoadDate;
	}

	public void setLastViralLoadDate(String lastViralLoadDate) {
		this.lastViralLoadDate = lastViralLoadDate;
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

	public Integer getAgeInYears() {
		return ageInYears;
	}

	public void setAgeInYears(Integer ageInYears) {
		this.ageInYears = ageInYears;
	}
}
