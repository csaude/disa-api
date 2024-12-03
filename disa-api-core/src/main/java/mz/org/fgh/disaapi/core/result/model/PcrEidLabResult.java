package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "PcrEidLabResult")
@DiscriminatorValue("PCR_EID")
public class PcrEidLabResult extends LabResult {

    @Column(name = "Attribute1")
    private String positivityLevel;

	public String getPositivityLevel() {
		return positivityLevel;
	}

	public void setPositivityLevel(String positivityLevel) {
		this.positivityLevel = positivityLevel;
	}
}
