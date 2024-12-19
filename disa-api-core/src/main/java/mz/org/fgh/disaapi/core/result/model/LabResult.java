/**
 *
 */
package mz.org.fgh.disaapi.core.result.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import mz.org.fgh.disaapi.core.config.ValidNid;
import mz.org.fgh.disaapi.core.hibernate.SampleTypeAttributeConverter;

/**
 * @author Stélio Moiane
 * @author Hélio Machabane
 *
 */
@ValidNid
@Entity
@Table(name = "VlData")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TypeOfResult", discriminatorType = DiscriminatorType.STRING, length = 50)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "typeOfResult")
@JsonSubTypes({
	@JsonSubTypes.Type(value = HIVVLLabResult.class, name="HIVVL"),
	@JsonSubTypes.Type(value = TBLamLabResult.class, name="TBLAM"),
	@JsonSubTypes.Type(value = CRAGLabResult.class, name="CRAG"),
	@JsonSubTypes.Type(value = CD4LabResult.class, name="CD4"),
	@JsonSubTypes.Type(value = PcrEidLabResult.class, name="PCR_EID")
})
public abstract class LabResult extends GenericEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "FinalResult")
	@NotBlank(message = "FinalResult cannot be null or empty")
	private String finalResult;

	@Column(name = "UNIQUEID")
	@NotBlank(message = "UNIQUEID cannot be null or empty")
	private String nid;

	@Column(name = "RequestID", unique = true)
	private String requestId;
	
	@Column(name = "EncounterID")
	private Integer encounterId;

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
	@Convert(converter = SampleTypeAttributeConverter.class)
	private SampleType sampleType;

	@Column(name = "ViralLoadResultCategory")
	private String viralLoadResultQualitative;

	@Column(name = "AuthorisedDateTime")
	private LocalDateTime labResultDate;

	@Column(name = "AuthorisedBy")
	private String aprovedBy;

	@Column(name = "ClinicalInfo")
	private String labComments;

	@Basic(optional = false)
	@Column(name = "VIRAL_LOAD_STATUS", columnDefinition = "enum('PENDING','PROCESSED','NOT_PROCESSED')")
	@Enumerated(EnumType.STRING)
	private LabResultStatus labResultStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "NOT_PROCESSING_CAUSE")
	private NotProcessingCause notProcessingCause;

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

	@Column(name = "SYNCHRONIZED_BY")
	private String synchronizedBy;

	@Column
	private Integer ageInYears;

	@Column(name = "TypeOfResult", insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private TypeOfResult typeOfResult;

	@Column(name = "RegisteredDateTime")
	private LocalDateTime registeredDateTime;
	
	@PrePersist
    protected void onPrePersist() {
        if (labResultStatus == null) {
            labResultStatus = LabResultStatus.PENDING;
        }
    }

	public String getSynchronizedBy() {
		return synchronizedBy;
	}

	public void setSynchronizedBy(String synchronizedBy) {
		this.synchronizedBy = synchronizedBy;
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
	
	public Integer getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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

	public SampleType getSampleType() {
		return sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
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

	public LocalDateTime getLabResultDate() {
		return labResultDate;
	}

	public void setLabResultDate(LocalDateTime labResultDate) {
		this.labResultDate = labResultDate;
	}

	public LabResultStatus getLabResultStatus() {
		return labResultStatus;
	}

	public void setLabResultStatus(LabResultStatus labResultStatus) {
		this.labResultStatus = labResultStatus;
	}

	public void setNotProcessed() {
		labResultStatus = LabResultStatus.NOT_PROCESSED;
	}

	public void setProcessed() {
		labResultStatus = LabResultStatus.PROCESSED;
	}

	public void setPending() {
		labResultStatus = LabResultStatus.PENDING;
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

	public void setCauseDuplicateNid() {
		notProcessingCause = NotProcessingCause.DUPLICATE_NID;
	}

	public void setCauseDuplicateReqId() {
		notProcessingCause = NotProcessingCause.DUPLICATED_REQUEST_ID;
	}

	public void setCauseInvalidResult() {
		notProcessingCause = NotProcessingCause.INVALID_RESULT;
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

	public TypeOfResult getTypeOfResult() {
		return typeOfResult;
	}

	public void setTypeOfResult(TypeOfResult typeOfResult) {
		this.typeOfResult = typeOfResult;
	}

	public String getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(String finalResult) {
		this.finalResult = finalResult;
	}

	public LocalDateTime getRegisteredDateTime() {
		return registeredDateTime;
	}

	public void setRegisteredDateTime(LocalDateTime registeredDateTime) {
		this.registeredDateTime = registeredDateTime;
	}
}
