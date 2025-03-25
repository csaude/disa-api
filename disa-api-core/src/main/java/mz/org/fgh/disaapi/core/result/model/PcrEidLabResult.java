package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import mz.org.fgh.disaapi.core.config.ValidPcrEidNid;

@Entity(name = "PcrEidLabResult")
@DiscriminatorValue("PCR_EID")
@ValidPcrEidNid
public class PcrEidLabResult extends LabResult {

    private static final long serialVersionUID = 1L;
    
	@Column(name = "Attribute1")
    private String positivityLevel;

	public String getPositivityLevel() {
		return positivityLevel;
	}

	public void setPositivityLevel(String positivityLevel) {
		this.positivityLevel = positivityLevel;
	}
}
