package mz.org.fgh.disaapi.core.result.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "TBLamLabResult")
@DiscriminatorValue("TBLAM")
public class TBLamLabResult extends LabResult {

    @Column(name = "Attribute1")
    private String positivityLevel;

    public String getPositivityLevel() {
        return positivityLevel;
    }

    public void setPositivityLevel(String positivityLevel) {
        this.positivityLevel = positivityLevel;
    }

}
